package com.example.tasksapp.presentation.screens.workSapcesList

sealed class WorkSpacesListEvent {
    object OnRefresh: WorkSpacesListEvent()
}