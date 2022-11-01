package com.example.tasksapp.presentation.screens.profile

import java.io.InputStream

sealed class ProfileEvent {
    object Refresh: ProfileEvent()
    object LogOut: ProfileEvent()
    data class UploadNewAvatarEvent(val stream: InputStream): ProfileEvent()
}