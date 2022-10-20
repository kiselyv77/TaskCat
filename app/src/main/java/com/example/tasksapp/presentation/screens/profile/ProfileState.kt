package com.example.tasksapp.presentation.screens.profile

data class ProfileState(
    val name: String = "",
    val login: String = "",
    val status: String = "",
    val error: String = "",
    val isLoading: Boolean = true,
    val isLogOut: Boolean = false
)
