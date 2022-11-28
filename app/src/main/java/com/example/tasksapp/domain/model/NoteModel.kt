package com.example.tasksapp.domain.model

data class NoteModel(
    val id: String,
    val info: String,
    val loginUser: String,
    val taskId: String,
    val attachmentFile: String,
    val dateTime: String,
    val isArrived: Boolean
)
