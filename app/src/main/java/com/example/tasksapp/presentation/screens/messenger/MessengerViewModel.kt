package com.example.tasksapp.presentation.screens.messenger

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MessengerViewModel @Inject constructor(

) : ViewModel() {

    private var client: HttpClient = HttpClient {
        install(WebSockets)
    }

    private val _state = mutableStateOf(MessengerState())
    val state: State<MessengerState> = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            client.ws("ws://6ce5-176-112-246-118.eu.ngrok.io/chat") {
                val messageOutputRoutine = launch {
                    for (message in incoming) {
                        message as? Frame.Text ?: continue
                        val messageList = _state.value.messagesList.toMutableList()
                        messageList.add(message.readText())
                        _state.value = _state.value.copy(messagesList = messageList)

                    }
                }
                val userInputRoutine = launch {
                    while (true) {
                        if (_state.value.send) {
                            _state.value = _state.value.copy(send = false)
                            val message = _state.value.inputMessage
                            send(message)
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

}
