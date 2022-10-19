package com.example.tasksapp.presentation.screens.usersList

import com.example.tasksapp.data.remote.dto.UserDTO

data class UserListState (
    val usersList: List<UserDTO> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false,
)