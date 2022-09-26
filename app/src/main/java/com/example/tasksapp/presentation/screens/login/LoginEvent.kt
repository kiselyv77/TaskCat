package com.example.tasksapp.presentation.screens.login

sealed class LoginEvent{
    object Login: LoginEvent()
    data class SetLogin(val newValue:String): LoginEvent()
    data class SetPassword(val newValue:String): LoginEvent()
}
