package com.example.tasksapp.presentation.screens.messenger

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.data.mappers.toMessageModel
import com.example.tasksapp.data.remote.Spec.BASE_URL
import com.example.tasksapp.data.remote.dto.MessageDTO
import com.example.tasksapp.domain.use_cases.GetMessagesFromWorkSpace
import com.example.tasksapp.domain.use_cases.GetUserByToken
import com.example.tasksapp.util.Resource
import com.example.tasksapp.util.generateRandomUUID
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val getUserByToken: GetUserByToken,
    private val getMessagesUseCase: GetMessagesFromWorkSpace,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var client: HttpClient = HttpClient {
        install(WebSockets) {
            contentConverter = GsonWebsocketContentConverter()
        }
    }

    private val _state = mutableStateOf(MessengerState())
    val state: State<MessengerState> = _state

    init {
        getMyLogin()
        getMessages()
        viewModelScope.launch(Dispatchers.IO) {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            client.ws("ws://$BASE_URL/chat/${Token.token}/$workSpaceId") {
                val messageOutputRoutine = launch {
                    while (true) {
                        val message = receiveDeserialized<MessageDTO>()
                        if(!_state.value.messagesList.any { it.id == message.id }){
                            //Пришло чужое сообщение
                            val messageList = _state.value.messagesList.toMutableList()
                            messageList.add(0, message.toMessageModel())
                            _state.value = _state.value.copy(messagesList = messageList)
                        }
                        else{
                            //Вернулось мое сообщение
                            val messageList = _state.value.messagesList.toMutableList()
                            val messageModel = message.toMessageModel()
                            val index = messageList.indexOf(messageModel.copy(isArrived = false))
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
                                        dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                                        sendingUser = _state.value.my.login,
                                        workSpaceId = workSpaceId,
                                        text = messageText
                                    )

                                val messageList = _state.value.messagesList.toMutableList()
                                messageList.add(0, messageDTO.toMessageModel().copy(isArrived = false))
                                _state.value = _state.value.copy(messagesList = messageList)

                                sendSerialized(
                                    messageDTO
                                )
                            }
                            _state.value = _state.value.copy(inputMessage = "")
                        }
                    }
                }

                userInputRoutine.join() // Wait for completion; either "exit" or error
                messageOutputRoutine.cancelAndJoin()
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
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            getMessagesUseCase(Token.token, workSpaceId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { messages ->
                            _state.value = _state.value.copy(
                                messagesList = messages,
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
}
