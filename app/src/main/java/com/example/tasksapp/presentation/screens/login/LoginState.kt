package com.example.tasksapp.presentation.screens.login

data class LoginState(
    val login:String="",
    val password:String="",

    val error:String = "",
    val isLoading:Boolean = false,
    val token:String = "",
)
