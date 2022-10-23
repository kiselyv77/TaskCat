package com.example.tasksapp.presentation.screens.messenger

import com.example.tasksapp.domain.model.MessageModel

data class MessengerState(
    val myLogin:String = "",
    val messagesList:List<MessageModel> = listOf<MessageModel>(),
    val inputMessage:String = "",
    val send:Boolean = false,
    val isLoading:Boolean = false,
    val error:String = "",
)
