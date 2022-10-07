package com.example.tasksapp.presentation.screens.addWorkSpace

sealed class AddWorkSpaceEvent {
    data class SetName(val newName: String) : AddWorkSpaceEvent()
    data class SetDescription(val newDescription: String) : AddWorkSpaceEvent()
    object AddWorkspace : AddWorkSpaceEvent()
}