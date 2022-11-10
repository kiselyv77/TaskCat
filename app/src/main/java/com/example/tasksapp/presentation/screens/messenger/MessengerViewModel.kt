package com.example.tasksapp.presentation.screens.messenger

import android.content.ContentValues
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
import com.example.tasksapp.util.VoiceRecorder
import com.example.tasksapp.util.generateRandomUUID
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
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
    private val voiceRecorder: VoiceRecorder
) : ViewModel() {

    private var client: HttpClient = HttpClient {
        install(WebSockets) {
            contentConverter = GsonWebsocketContentConverter()
        }
    }
    private var offset = -10 // Этот офсет будет увеличиватся на 10 при каждом новом запросе или на 1 приприеме сообщения

    private val _state = mutableStateOf(MessengerState())
    val state: State<MessengerState> = _state

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
                        while (true) {
                            if (_state.value.send) {
                                _state.value = _state.value.copy(send = false)
                                val messageText = _state.value.inputMessage
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
                                _state.value = _state.value.copy(inputMessage = "")
                            }
                            if(_state.value.sendVoice){
                                _state.value = _state.value.copy(sendVoice = false)
                                val messageId = _state.value.voiceFile
                                val messageDTO = MessageDTO(
                                    id = generateRandomUUID(),
                                    userName = _state.value.my.name,
                                    dateTime = LocalDateTime.now()
                                        .format(DateTimeFormatter.ISO_DATE_TIME),
                                    sendingUser = _state.value.my.login,
                                    workSpaceId = workSpaceId,
                                    text = "",
                                    type = MessageTypes.MESSAGE_VOICE,
                                    fileName = messageId
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
                                _state.value = _state.value.copy(voiceFile = "")
                            }
                        }
                    }
                    userInputRoutine.join() // Wait for completion; either "exit" or error
                    messageOutputRoutine.cancelAndJoin()
                }
            } catch (e: ClosedReceiveChannelException) {
                Log.w(ContentValues.TAG, "Failure: ${e.message}")
            } catch (e: NoTransformationFoundException) {
                //Возникает если слишком часто открывать и закрывать чат
                // причина 429 Too Many Requests
                Log.w(ContentValues.TAG, "Failure: ${e.message}")
            } catch (e: Exception) {
                Log.w(ContentValues.TAG, "Failure: ${e.message}")
            }
        }
    }

    fun onEvent(event: MessengerEvent) {
        when (event) {
            MessengerEvent.Send -> {
                _state.value = _state.value.copy(send = true)
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
        }
    }

    private fun getMyLogin() {
        viewModelScope.launch {
            getUserByToken(Token.token).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { userDto ->
                            _state.value = _state.value.copy(
                                my = userDto,
                            )
                            getMessages()
                        }
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
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

    private fun startRecord(messageId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            voiceRecorder.startRecord(messageId)
        }
    }

    private fun stopRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            val file = voiceRecorder.stopRecord()
            sendVoiceMessageFile(file)
            _state.value = _state.value.copy(sendVoice = true)
            _state.value = _state.value.copy(voiceFile = file.name)
        }
    }

    private fun sendVoiceMessageFile(file: File){
        viewModelScope.launch(Dispatchers.IO) {
            val stream: InputStream = FileInputStream(file)
            uploadFileVoiceMessage(Token.token, stream, "${file.name}.mp3").collect{ result ->
                Log.d("dsfvsedfsrvsdfsv", result.data.toString())
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {}
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                }
            }
        }
    }
}
