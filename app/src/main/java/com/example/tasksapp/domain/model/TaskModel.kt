package com.example.tasksapp.domain.model

data class TaskModel(
    val id: String,
    val name: String,
    val description: String,
    val taskStatus: String,
    val deadLine: String
)