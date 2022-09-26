package com.example.tasksapp.presentation.screens.start

data class StartState(
    val isLoading:Boolean = true,
    val error:String = "",
    val isTokenValid: Boolean = false,
)
