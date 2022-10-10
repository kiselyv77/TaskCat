package com.example.tasksapp.presentation.screens.workSpaceDetail

import com.example.tasksapp.data.remote.dto.TaskDTO
import com.example.tasksapp.data.remote.dto.WorkSpaceDTO

data class WorkSpaceDetailState(
    val workspaceDetail: WorkSpaceDTO = WorkSpaceDTO("", "", "", "", emptyList(), emptyList()),
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
    val error:String = "",
    val dialogState: DialogState = DialogState(),
    val tasksState: TasksState = TasksState(),

)

data class TasksState(
    val isSuccess:Boolean = false,
    val error:String = "",
    val isLoading:Boolean = false,
    val tasks:List<TaskDTO> = emptyList(),
)

data class DialogState(
    val isOpen:Boolean = false,
    val name:String = "",
    val description:String = "",
    val error:String = "",
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
)
