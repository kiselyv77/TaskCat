package com.example.tasksapp.presentation.screens.taskDetail

import com.example.tasksapp.domain.model.UserModel
import java.io.InputStream
import java.time.LocalDateTime

sealed class TaskDetailEvent {
    object OnAllRefresh : TaskDetailEvent()
    object OnUsersRefresh : TaskDetailEvent()
    object ShowMore : TaskDetailEvent()
    data class SetInputText(val newText: String) : TaskDetailEvent()
    data class SetTaskStatusDialog(val newStatus: String) : TaskDetailEvent()
    object OpenCloseSetTaskStatusDialog : TaskDetailEvent()
    object SendNote : TaskDetailEvent()
    object SetTaskStatus : TaskDetailEvent()
    data class SetTaskDeadLine(val newDeadLine: LocalDateTime) : TaskDetailEvent()

    object OpenCloseAddUserToTaskDialog : TaskDetailEvent()
    data class AddUserToTask(val userLogin: String) : TaskDetailEvent()

    data class OpenCloseUserItemDialog(val userModel: UserModel = UserModel.getEmptyModel()) : TaskDetailEvent()

    object DeleteUser : TaskDetailEvent()

    object OpenCloseDeleteTaskDialog : TaskDetailEvent()

    object DeleteTask : TaskDetailEvent()

    object OpenCloseLeaveFromTaskDialog : TaskDetailEvent()

    object LeaveFromTask : TaskDetailEvent()

    data class AttachFile(val inputStream: InputStream, val originalFileName: String) : TaskDetailEvent()

    data class DownloadFile(val fileName: String) : TaskDetailEvent()

    object DetachFile : TaskDetailEvent()

}
