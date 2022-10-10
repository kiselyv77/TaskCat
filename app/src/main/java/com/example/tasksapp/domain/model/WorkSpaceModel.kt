package com.example.tasksapp.domain.model

data class WorkSpaceModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val creator: String = "",
    val users: List<String> = emptyList(),
    val tasks: List<String> = emptyList()
)
