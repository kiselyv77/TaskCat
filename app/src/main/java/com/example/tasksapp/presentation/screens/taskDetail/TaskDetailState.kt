package com.example.tasksapp.presentation.screens.taskDetail

import com.example.tasksapp.domain.model.TaskModel
import com.example.tasksapp.domain.model.UserModel

data class TaskDetailState(
    val task: TaskModel = TaskModel("", "", "", ""),
    val usersState: UsersState = UsersState(),
    val error: String = "",
    val isLoading: Boolean = false
)

data class UsersState(
    val users: List<UserModel> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)

