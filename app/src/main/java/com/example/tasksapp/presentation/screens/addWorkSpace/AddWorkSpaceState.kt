package com.example.tasksapp.presentation.screens.addWorkSpace

import com.example.tasksapp.data.remote.dto.WorkSpaceDTO

data class AddWorkSpaceState(
    val name: String = "",
    val description: String = "",
    val error: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val workSpace: WorkSpaceDTO = WorkSpaceDTO("", "", "", "", emptyList(), emptyList()),
)
