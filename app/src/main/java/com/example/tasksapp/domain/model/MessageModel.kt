package com.example.tasksapp.domain.model

import com.example.tasksapp.data.remote.dto.MessageDTO

data class MessageModel(
    val id:String,
    val userName: String,
    val sendingUser: String,
    val workSpaceId: String,
    val dateTime:String,
    val text: String,
    val isArrived: Boolean
)

fun MessageDTO.toMessageModel():MessageModel{
    return MessageModel(
        id =  id,
        userName = userName,
        sendingUser= sendingUser,
        workSpaceId= workSpaceId,
        dateTime= dateTime,
        text= text,
        isArrived = true
    )
}

