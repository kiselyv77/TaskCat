package com.example.tasksapp.presentation.screens.messenger

import com.example.tasksapp.data.remote.dto.UserDTO
import com.example.tasksapp.domain.model.MessageModel

data class MessengerState(
    val my: UserDTO = UserDTO("", "", ""),
    val messagesList: List<MessageModel> = listOf<MessageModel>(),
    val inputMessage: String = "",
    val send: Boolean = false,
    val isLoading: Boolean = true,
    val error: String = "",
)
