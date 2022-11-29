package com.example.tasksapp.data.remote.dto

data class AddTaskReceiveDTO(
    val token: String,
    val name: String,
    val description:String,
    val workSpaceId: String,
    val deadLine: String
)