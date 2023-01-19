package com.example.tasksapp.data.mappers

import com.example.tasksapp.data.remote.dto.NoteDTO
import com.example.tasksapp.domain.model.NoteModel

fun NoteDTO.toNoteModel(): NoteModel{
    return NoteModel(
        id = id,
        info = info,
        loginUser = loginUser,
        userName = userName,
        taskId = taskId,
        attachmentFile = attachmentFile,
        timeStamp = timeStamp,
        isArrived = true
    )
}