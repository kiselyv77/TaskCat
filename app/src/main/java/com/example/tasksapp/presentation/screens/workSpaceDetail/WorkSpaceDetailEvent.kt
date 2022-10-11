package com.example.tasksapp.presentation.screens.workSpaceDetail

sealed class WorkSpaceDetailEvent {
    object OnRefresh: WorkSpaceDetailEvent()
    object OpenCloseAddTaskDialog: WorkSpaceDetailEvent()
    object OpenCloseAddUserDialog: WorkSpaceDetailEvent()
    data class SetTaskNameInDialog(val newName: String): WorkSpaceDetailEvent()
    data class SetTaskDescriptionInDialog(val newDescription:String): WorkSpaceDetailEvent()
    data class SetUserLoginInDialog(val newUserLogin:String): WorkSpaceDetailEvent()
    object AddTask: WorkSpaceDetailEvent()
    object AddUser: WorkSpaceDetailEvent()

}