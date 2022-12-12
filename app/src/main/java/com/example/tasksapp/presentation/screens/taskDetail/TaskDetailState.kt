package com.example.tasksapp.presentation.screens.taskDetail

import com.example.tasksapp.domain.model.NoteModel
import com.example.tasksapp.domain.model.TaskModel
import com.example.tasksapp.domain.model.UserModel
import com.example.tasksapp.presentation.commonComponents.SetTaskStatusDialogState

data class TaskDetailState(
    val task: TaskModel = TaskModel("", "", "", "", ""),
    val my: UserModel = UserModel("", "", "", "", ""),
    val notesList: List<NoteModel> = listOf<NoteModel>(),
    val usersState: UsersState = UsersState(),
    val error: String = "",
    val isLoading: Boolean = false,
    val setTaskStatusDialogState: SetTaskStatusDialogState = SetTaskStatusDialogState(),
    val addUserDialogState:AddUserToTaskDialogState = AddUserToTaskDialogState(),
    val inputText: String = ""
)

data class UsersState(
    val users: List<UserModel> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)

data class AddUserToTaskDialogState(
    val error: String = "",
    val isLoading: Boolean = false,
    val isSuccess:Boolean = false,
    val isOpen:Boolean = false,
    val usersFromWorkSpaceList:List<UserModel> = emptyList()
)



