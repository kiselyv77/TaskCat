package com.example.tasksapp.presentation.screens.usersList

import com.example.tasksapp.domain.model.UserModel

data class UserListState (
    val usersList: List<UserModel> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false,
)