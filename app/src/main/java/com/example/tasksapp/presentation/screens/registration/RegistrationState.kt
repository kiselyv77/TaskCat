package com.example.tasksapp.presentation.screens.registration

data class RegistrationState(
    val name: String = "",
    val login: String = "",
    val password: String = "",
    val error:String = "",
    val isLoading:Boolean = false,
    val token:String = ""
)

