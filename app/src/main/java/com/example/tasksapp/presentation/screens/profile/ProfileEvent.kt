package com.example.tasksapp.presentation.screens.profile

sealed class ProfileEvent {
    object Refresh: ProfileEvent()
    object LogOut: ProfileEvent()
}