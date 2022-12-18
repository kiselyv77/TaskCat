package com.example.tasksapp.presentation.screens.usersList

sealed class UserListEvent {
    object OnRefresh: UserListEvent()
    object SetUserStatusToWorkSpace: UserListEvent()
    object DeleteUser : UserListEvent()
    data class CloseOpenDialog(val userLogin:String = "", val currentStatus:String = ""): UserListEvent()
}