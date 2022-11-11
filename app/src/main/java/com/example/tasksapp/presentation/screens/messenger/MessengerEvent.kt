package com.example.tasksapp.presentation.screens.messenger

sealed class MessengerEvent{
    object Send: MessengerEvent()
    object Refresh : MessengerEvent()

    object StartVoiceRecord:MessengerEvent()
    object StopVoiceRecord:MessengerEvent()

    data class PlayPauseVoiceMessage(val messageId: String) :MessengerEvent()

    data class SetMessage(val newMessage:String): MessengerEvent()
}
