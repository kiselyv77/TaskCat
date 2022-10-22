package com.example.tasksapp.presentation.screens.messenger

import com.example.tasksapp.data.remote.dto.MessageDTO

data class MessengerState(
    val myLogin:String = "",
    val messagesList:List<MessageDTO> = listOf<MessageDTO>(),
    val inputMessage:String = "",
    val send:Boolean = false
)
