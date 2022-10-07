package com.example.tasksapp.presentation.screens.addWorkSpace

data class AddWorkSpaceState(
    val name: String = "",
    val description: String = "",
    val error: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)
