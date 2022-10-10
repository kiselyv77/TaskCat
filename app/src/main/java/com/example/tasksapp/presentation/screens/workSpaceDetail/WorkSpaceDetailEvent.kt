package com.example.tasksapp.presentation.screens.workSpaceDetail

sealed class WorkSpaceDetailEvent {
    object OnRefresh: WorkSpaceDetailEvent()
    object OpenCloseDialog: WorkSpaceDetailEvent()
    data class SetTaskNameInDialog(val newName: String): WorkSpaceDetailEvent()
    data class SetTaskDescriptionInDialog(val newDescription:String): WorkSpaceDetailEvent()
    object AddTask: WorkSpaceDetailEvent()

}