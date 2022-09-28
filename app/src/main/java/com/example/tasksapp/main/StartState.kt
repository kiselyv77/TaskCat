package com.example.tasksapp.main

data class StartState(
    val isLoading:Boolean = true,
    val error:String = "",
    val isTokenValid: Boolean = false,
)
