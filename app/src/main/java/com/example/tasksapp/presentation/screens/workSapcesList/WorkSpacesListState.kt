package com.example.tasksapp.presentation.screens.workSapcesList

import com.example.tasksapp.data.remote.dto.WorkSpaceDTO

data class WorkSpacesListState(
    val workSpacesList: List<WorkSpaceDTO> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false,
)
