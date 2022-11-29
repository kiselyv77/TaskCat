package com.example.tasksapp.data.remote.dto

data class NoteDTO(
    val id: String,
    val info: String,
    val loginUser: String,
    val userName: String = "",
    val taskId: String,
    val attachmentFile: String,
    val dateTime: String
)
