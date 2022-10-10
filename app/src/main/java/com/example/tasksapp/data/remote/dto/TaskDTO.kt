package com.example.tasksapp.data.remote.dto

data class TaskDTO(
    val id: String,
    val name:String,
    val description:String,
    val users:List<String>,
    val subTask:List<String>,
    val taskStatus:String
)
