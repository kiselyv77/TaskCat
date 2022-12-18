package com.example.tasksapp.presentation.screens.taskDetail

import java.time.LocalDateTime

sealed class TaskDetailEvent {
    object OnAllRefresh : TaskDetailEvent()
    object OnUsersRefresh  : TaskDetailEvent()
    object ShowMore : TaskDetailEvent()
    data class SetInputText(val newText: String) : TaskDetailEvent()
    data class SetTaskStatusDialog(val newStatus: String) : TaskDetailEvent()
    object OpenCloseSetTaskStatusDialog : TaskDetailEvent()
    object SendNote : TaskDetailEvent()
    object SetTaskStatus : TaskDetailEvent()
    data class SetTaskDeadLine(val newDeadLine: LocalDateTime) : TaskDetailEvent()

    object OpenCloseAddUserToTaskDialog: TaskDetailEvent()
    object OpenCloseDeleteTaskDialog : TaskDetailEvent()
    object DeleteTask : TaskDetailEvent()
    data class AddUserToTask(val userLogin: String) : TaskDetailEvent()

}
