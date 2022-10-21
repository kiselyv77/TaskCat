package com.example.tasksapp.presentation.screens.messenger

data class MessengerState(
    val messagesList:List<String> = listOf<String>(),
    val inputMessage:String = "",
    val send:Boolean = false
)
