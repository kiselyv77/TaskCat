package com.example.tasksapp.presentation.screens.workSpaceDetail

import com.example.tasksapp.data.remote.dto.TaskDTO
import com.example.tasksapp.data.remote.dto.WorkSpaceDTO

data class WorkSpaceDetailState(
    val workspaceDetail: WorkSpaceDTO = WorkSpaceDTO("", "", "", "", emptyList(), emptyList()),
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
    val error:String = "",
    val addTaskDialogState: AddTaskDialogState = AddTaskDialogState(),
    val addUserDialogState: AddUserDialogState = AddUserDialogState(),
    val tasksState: TasksState = TasksState(),

    )

data class TasksState(
    val isSuccess:Boolean = false,
    val error:String = "",
    val isLoading:Boolean = false,
    val tasks:List<TaskDTO> = emptyList(),
)

data class AddTaskDialogState(
    val isOpen:Boolean = false,
    val name:String = "",
    val description:String = "",
    val error:String = "",
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
)

data class AddUserDialogState(
    val isOpen:Boolean = false,
    val userLogin:String = "",
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
    val error:String = "",
)
