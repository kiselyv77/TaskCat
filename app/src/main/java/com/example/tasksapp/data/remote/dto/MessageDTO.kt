package com.example.tasksapp.data.remote.dto

data class MessageDTO(
    val id:String,
    val userName: String,
    val sendingUser: String,
    val workSpaceId: String,
    val type: String,
    val timeStamp:String,
    val text: String,
    val fileName: String
)