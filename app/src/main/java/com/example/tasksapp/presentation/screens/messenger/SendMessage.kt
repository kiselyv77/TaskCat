package com.example.tasksapp.presentation.screens.messenger

sealed class SendMessage {
    data class SendText(val text: String) : SendMessage()
    data class SendVoice(val fileName: String) : SendMessage()
}
