package com.example.tasksapp.data.mappers

import com.example.tasksapp.data.remote.dto.MessageDTO
import com.example.tasksapp.domain.model.MessageModel

fun MessageDTO.toMessageModel(): MessageModel {
    return MessageModel(
        id =  id,
        userName = userName,
        sendingUser= sendingUser,
        workSpaceId= workSpaceId,
        timeStamp = timeStamp,
        text = text,
        isArrived = true,
        type = type,
        fileName = fileName
    )
}