package com.example.tasksapp.presentation.screens.messenger

sealed class MessengerEvent{
    object Send: MessengerEvent()

    data class SetMessage(val newMessage:String):MessengerEvent()
}
