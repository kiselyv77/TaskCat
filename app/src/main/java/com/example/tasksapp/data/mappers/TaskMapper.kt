package com.example.tasksapp.data.mappers

import com.example.tasksapp.data.remote.dto.TaskDTO
import com.example.tasksapp.domain.model.TaskModel

fun TaskDTO.toTaskModel(): TaskModel {
    return TaskModel(
        id = id,
        name = name,
        description = description,
        taskStatus = taskStatus,
        deadLine = deadLine
    )
}