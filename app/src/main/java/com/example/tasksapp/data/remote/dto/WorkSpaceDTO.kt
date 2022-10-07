package com.example.tasksapp.data.remote.dto

data class WorkSpaceDTO(
    val id: String,
    val name: String,
    val description: String,
    val creator: String,
    val users: List<String>,
    val tasks: List<String>
)
