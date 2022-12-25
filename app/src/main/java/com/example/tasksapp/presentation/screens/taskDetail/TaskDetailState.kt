package com.example.tasksapp.presentation.screens.taskDetail

import com.example.tasksapp.domain.model.NoteModel
import com.example.tasksapp.domain.model.TaskModel
import com.example.tasksapp.domain.model.UserModel
import com.example.tasksapp.presentation.commonComponents.CustomAlertDialogState
import com.example.tasksapp.presentation.commonComponents.SetTaskStatusDialogState
import java.io.InputStream

data class TaskDetailState(
    val task: TaskModel = TaskModel("", "", "", "", ""),
    val my: UserModel = UserModel("", "", "", "", ""),
    val notesList: List<NoteModel> = listOf<NoteModel>(),
    val usersState: UsersState = UsersState(),
    val error: String = "",
    val isLoading: Boolean = false,
    val setTaskStatusDialogState: SetTaskStatusDialogState = SetTaskStatusDialogState(),
    val addUserDialogState:AddUserToTaskDialogState = AddUserToTaskDialogState(),
    val deleteTaskDialogState: CustomAlertDialogState = CustomAlertDialogState(),
    val leaveFromTaskDialogState: CustomAlertDialogState = CustomAlertDialogState(),
    val userItemDialogState: UserItemDialogState2 = UserItemDialogState2(),
    val inputText: String = "",
    val attachmentFileInfo:AttachmentFileInfo = AttachmentFileInfo()
)

data class AttachmentFileInfo(
    val attachmentFile: InputStream? = null,
    val originalFileName:String = ""
)

data class UsersState(
    val users: List<UserModel> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)

data class UserItemDialogState2(
    val userModel: UserModel = UserModel.getEmptyModel(),
    val isOpen:Boolean = false,
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
    val error:String = ""
)

data class AddUserToTaskDialogState(
    val error: String = "",
    val isLoading: Boolean = true,
    val isSuccess:Boolean = false,
    val isOpen:Boolean = false,
    val displayedList:List<UserModel> = emptyList()
)



