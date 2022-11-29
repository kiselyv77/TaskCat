package com.example.tasksapp.data.remote.dto

data class TaskDTO(
    val id: String,
    val name:String,
    val description:String,
    val taskStatus:String,
    val deadLine: String,
    val creationDate: String

)
