package com.example.tasksapp.presentation.screens.workSapcesList

import com.example.tasksapp.domain.model.WorkSpaceModel

data class WorkSpacesListState(
    val workSpacesList: List<WorkSpaceModel> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false,

)
