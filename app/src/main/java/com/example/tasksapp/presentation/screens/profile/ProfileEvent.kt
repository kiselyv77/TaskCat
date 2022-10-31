package com.example.tasksapp.presentation.screens.profile

import android.net.Uri

sealed class ProfileEvent {
    object Refresh: ProfileEvent()
    object LogOut: ProfileEvent()
    data class UploadNewAvatarEvent(val image: Uri?): ProfileEvent()
}