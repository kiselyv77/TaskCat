package com.example.tasksapp.data.remote.dto

data class MessageReceiveDTO(
    val sendingUser: String,
    val workSpaceId: String,
    val text: String,
)


data class MessageResponseDTO(
    val id:String,
    val userName: String,
    val sendingUser: String,
    val workSpaceId: String,
    val dateTime:String,
    val text: String
)