package com.example.tasksapp.domain.model

import com.example.tasksapp.data.remote.dto.MessageResponseDTO

data class MessageModel(
    val id:String,
    val userName: String,
    val sendingUser: String,
    val workSpaceId: String,
    val dateTime:String,
    val text: String
)

fun MessageResponseDTO.toMessageModel():MessageModel{
    return MessageModel(
        id =  id,
        userName = userName,
        sendingUser= sendingUser,
        workSpaceId= workSpaceId,
        dateTime= dateTime,
        text= text
    )
}

