package com.example.tasksapp.domain.model

data class MessageModel(
    val id:String,
    val userName: String,
    val sendingUser: String,
    val workSpaceId: String,
    val timeStamp:String,
    val text: String,
    val isArrived: Boolean,
    val type: String,
    val fileName:String,
    val progress: Float = 0f
)



