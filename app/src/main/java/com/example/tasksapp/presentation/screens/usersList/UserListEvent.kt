package com.example.tasksapp.presentation.screens.usersList

sealed class UserListEvent {
    object OnRefresh: UserListEvent()
}