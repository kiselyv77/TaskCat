package com.example.tasksapp.presentation.screens.messenger

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.data.mappers.toMessageModel
import com.example.tasksapp.data.remote.Spec
import com.example.tasksapp.data.remote.dto.MessageDTO
import com.example.tasksapp.domain.use_cases.GetMessagesFromWorkSpace
import com.example.tasksapp.domain.use_cases.GetUserByToken
import com.example.tasksapp.domain.use_cases.UploadFileVoiceMessage
import com.example.tasksapp.util.MessageTypes
import com.example.tasksapp.util.Resource
import com.example.tasksapp.util.generateRandomUUID
import com.example.tasksapp.util.media.MediaRecordResult
import com.example.tasksapp.util.media.VoicePlayer
import com.example.tasksapp.util.media.VoiceRecorder
import com.example.tasksapp.util.vibration.VibrationFeedBack
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val getUserByToken: GetUserByToken,
    private val getMessagesUseCase: GetMessagesFromWorkSpace,
    private val uploadFileVoiceMessage: UploadFileVoiceMessage,
    private val savedStateHandle: SavedStateHandle,
    private val voiceRecorder: VoiceRecorder,
    private val voicePlayer: VoicePlayer,
    private val vibrationFeedBack: VibrationFeedBack
) : ViewModel() {


    private var client: HttpClient = HttpClient {
        install(WebSockets) {
            contentConverter = GsonWebsocketContentConverter()
        }
    }
    private var offset =
        -10 // Этот офсет будет увеличиватся на 10 при каждом новом запросе или на 1 приприеме сообщения
    private lateinit var jobStartRecord: Job

    private val _state = mutableStateOf(MessengerState())
    val state: State<MessengerState> = _state

    private val _messagesFlow = MutableSharedFlow<SendMessage>()

    init {
        getMyLogin()
        viewModelScope.launch(Dispatchers.IO) {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            try {
                client.ws("ws://${Spec.BASE_URL}/chat/${Token.token}/$workSpaceId") {
                    val messageOutputRoutine = launch {
                        while (true) {
                            val message = receiveDeserialized<MessageDTO>()
                            if (!_state.value.messagesList.any { it.id == message.id }) {
                                //Пришло чужое сообщение
                                offset++
                                val messageList = _state.value.messagesList.toMutableList()
                                messageList.add(0, message.toMessageModel())
                                _state.value = _state.value.copy(messagesList = messageList)
                            } else {
                                //Вернулось мое сообщение
                                offset++
                                val messageList = _state.value.messagesList.toMutableList()
                                val messageModel = message.toMessageModel()
                                val index =
                                    messageList.indexOf(messageModel.copy(isArrived = false))
                                messageList[index] = messageModel.copy(isArrived = true)
                                _state.value = _state.value.copy(messagesList = messageList)
                            }
                        }
                    }
                    val userInputRoutine = launch {
                        _messagesFlow.collectLatest { sendMessage ->
                            when (sendMessage) {
                                is SendMessage.SendText -> {
                                    val messageText = sendMessage.text
                                    if (messageText.isNotEmpty()) {
                                        val messageDTO = MessageDTO(
                                            id = generateRandomUUID(),
                                            userName = _state.value.my.name,
                                            dateTime = LocalDateTime.now()
                                                .format(DateTimeFormatter.ISO_DATE_TIME),
                                            sendingUser = _state.value.my.login,
                                            workSpaceId = workSpaceId,
                                            text = messageText,
                                            type = MessageTypes.MESSAGE_TEXT,
                                            fileName = ""
                                        )

                                        val messageList = _state.value.messagesList.toMutableList()
                                        messageList.add(
                                            0,
                                            messageDTO.toMessageModel().copy(isArrived = false)
                                        )
                                        _state.value = _state.value.copy(messagesList = messageList)

                                        sendSerialized(
                                            messageDTO
                                        )
                                    }
                                }
                                is SendMessage.SendVoice -> {
                                    val messageId = sendMessage.fileName
                                    val messageDTO = MessageDTO(
                                        id = messageId,
                                        userName = _state.value.my.name,
                                        dateTime = LocalDateTime.now()
                                            .format(DateTimeFormatter.ISO_DATE_TIME),
                                        sendingUser = _state.value.my.login,
                                        workSpaceId = workSpaceId,
                                        text = "",
                                        type = MessageTypes.MESSAGE_VOICE,
                                        fileName = "$messageId.mp3"
                                    )
                                    val messageList = _state.value.messagesList.toMutableList()
                                    messageList.add(
                                        0,
                                        messageDTO.toMessageModel().copy(isArrived = false)
                                    )
                                    _state.value = _state.value.copy(messagesList = messageList)

                                    sendSerialized(
                                        messageDTO
                                    )
                                }
                            }
                        }
                    }
                    userInputRoutine.join() // Wait for completion; either "exit" or error
                    messageOutputRoutine.cancelAndJoin()
                }
            } catch (e: ClosedReceiveChannelException) {
                Log.w("WebSocketSession", "Failure: ${e.message}")
            } catch (e: NoTransformationFoundException) {
                //Возникает если слишком часто открывать и закрывать чат
                // причина 429 Too Many Requests
                Log.w("WebSocketSession", "Failure: ${e.message}")
            } catch (e: Exception) {
                Log.w("WebSocketSession", "Failure: ${e.message}")
            }
        }
    }

    fun onEvent(event: MessengerEvent) {
        when (event) {
            MessengerEvent.Send -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _messagesFlow.emit(SendMessage.SendText(_state.value.inputMessage))
                    _state.value = _state.value.copy(inputMessage = "")
                }
            }
            is MessengerEvent.SetMessage -> {
                _state.value = _state.value.copy(inputMessage = event.newMessage)
            }
            MessengerEvent.Refresh -> {
                getMessages()
            }
            MessengerEvent.StartVoiceRecord -> {
                Log.w("voicemesseging", "start")
                _state.value = _state.value.copy(isVoiceRecording = true)
                val messageId = generateRandomUUID()
                startRecord(messageId)
            }
            MessengerEvent.StopVoiceRecord -> {
                Log.w("voicemesseging", "stop")
                _state.value = _state.value.copy(isVoiceRecording = false)
                stopRecord()
            }
            is MessengerEvent.PlayPauseVoiceMessage -> {
                if (event.messageId == _state.value.voiceMessagesState.currentMessageId && _state.value.voiceMessagesState.playing) {
                    _state.value = _state.value.copy(
                        voiceMessagesState = _state.value.voiceMessagesState.copy(playing = false)
                    )
                    pauseVoiceMessage()
                } else {
                    Log.d("ssadfsdfsf", "play")
                    _state.value = _state.value.copy(
                        voiceMessagesState = state.value.voiceMessagesState.copy(currentMessageId = event.messageId, playing = true),
                        playingVoiceMessageProgress = 0f
                    )
                    playVoiceMessage(event.messageId)
                }
            }
            is MessengerEvent.SeekTo -> {
                seekTo(event.progress)
            }
        }
    }

    private fun seekTo(progress: Float) {
        viewModelScope.launch {
            voicePlayer.seekTo(progress)
        }
    }

    private fun getMyLogin() {
        viewModelScope.launch() {
            getUserByToken(Token.token).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(error = "", isLoading = false)
                        result.data?.let { userDto ->
                            _state.value = _state.value.copy(
                                my = userDto,
                            )
                            getMessages()
                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(error = result.message.toString(), isLoading = false)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(error = "", isLoading = true)
                    }
                }
            }
        }
    }

    private fun getMessages() {
        offset += 10
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            getMessagesUseCase(Token.token, workSpaceId, offset).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val messageList = _state.value.messagesList.toMutableList()
                        result.data?.let { messages ->
                            messageList.addAll(messages)
                            _state.value = _state.value.copy(
                                messagesList = messageList,
                                error = result.message ?: "",
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(
                                error = result.message ?: "", isLoading = false
                            )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = result.isLoading,
                            error = result.message ?: ""
                        )
                    }
                }
            }
        }
    }

    private fun playVoiceMessage(messageId: String) {
        val fileName = _state.value.messagesList.last { it.id == messageId }.fileName
        viewModelScope.launch(Dispatchers.IO) {
            val url = "https://${Spec.BASE_URL}/getVoiceMessage/$fileName"
            voicePlayer.play(url).collect { progress ->
                _state.value = _state.value.copy(playingVoiceMessageProgress = progress)
                if (progress == 1f) {
                    _state.value = _state.value.copy(
                        playingVoiceMessageProgress = 0f,
                        voiceMessagesState = _state.value.voiceMessagesState.copy(playing = false)
                    )
                }
            }
        }
    }

    private fun pauseVoiceMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            voicePlayer.pause()
        }
    }

    private fun startRecord(messageId: String) {
        jobStartRecord = viewModelScope.launch(Dispatchers.IO) {
            vibrationFeedBack.startVibration(100L)
            voiceRecorder.startRecord(messageId).collect { mediaRecorderParam ->
                Log.d("mediaRecorderParam", mediaRecorderParam.toString())
                _state.value = _state.value.copy(
                    voiceRecordAmplitude = mediaRecorderParam.amplitude.toFloat() / 200,
                    voiceRecordTime = mediaRecorderParam.time
                )
            }
        }
    }

    private fun stopRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            jobStartRecord.cancelAndJoin()
            val recordResult = voiceRecorder.stopRecord()
            when (recordResult) {
                is MediaRecordResult.RecordSuccess -> {
                    _state.value = _state.value.copy(recordError = "")
                    recordResult.fileName?.let {
                        recordResult.stream?.let {
                            _messagesFlow.emit(SendMessage.SendVoice(recordResult.fileName))
                            sendVoiceMessageFile(recordResult.stream, recordResult.fileName)
                        }
                    }
                }
                is MediaRecordResult.RecordError -> {
                    _state.value =
                        _state.value.copy(recordError = "Ошибка записи удерживайте кнопку записи")
                    delay(1000)
                    _state.value = _state.value.copy(recordError = "")
                    Log.d("stopRecord", "Ошибка записи удерживайте кнопку записи")
                }
            }
        }
    }

    private fun sendVoiceMessageFile(stream: InputStream, fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            uploadFileVoiceMessage(Token.token, stream, "${fileName}.mp3").collect { result ->
                Log.d("dsfvsedfsrvsdfsv", result.data.toString())
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(error = "", isLoading = false)
                        result.data?.let {}
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(error = result.message.toString(), isLoading = false)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(error = "")
                    }
                }
            }
        }
    }
}
