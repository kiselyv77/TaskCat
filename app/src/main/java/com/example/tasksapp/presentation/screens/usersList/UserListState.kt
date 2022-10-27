package com.example.tasksapp.presentation.screens.usersList

import com.example.tasksapp.domain.model.UserModel

data class UserListState (
    val usersList: List<UserModel> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false,
    val myLogin:String = "",
    val dialogState: SetUserStatusToWorkSpaceDialogState = SetUserStatusToWorkSpaceDialogState()
)

data class SetUserStatusToWorkSpaceDialogState(
    val userLogin:String = "",
    val currentStatus:String = "",
    val isOpen:Boolean = false,
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
    val error:String = ""
)