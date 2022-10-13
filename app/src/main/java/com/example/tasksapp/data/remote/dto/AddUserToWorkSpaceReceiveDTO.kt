package com.example.tasksapp.data.remote.dto

data class AddUserToWorkSpaceReceiveDTO(
    val token: String,
    val invitedUserLogin: String,
    val workSpaceId: String
)
