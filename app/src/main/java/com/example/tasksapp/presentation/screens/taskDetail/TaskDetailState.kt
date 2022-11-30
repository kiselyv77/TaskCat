package com.example.tasksapp.presentation.screens.taskDetail

import com.example.tasksapp.domain.model.NoteModel
import com.example.tasksapp.domain.model.TaskModel
import com.example.tasksapp.domain.model.UserModel

data class TaskDetailState(
    val task: TaskModel = TaskModel("", "", "", "", ""),
    val my: UserModel = UserModel("", "", "", ""),
    val notesList: List<NoteModel> = listOf<NoteModel>(),
    val usersState: UsersState = UsersState(),
    val error: String = "",
    val isLoading: Boolean = false,

    val inputText: String = ""
)

data class UsersState(
    val users: List<UserModel> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)

