package com.example.tasksapp.presentation.screens.workSpaceDetail

import com.example.tasksapp.domain.model.TaskModel
import com.example.tasksapp.domain.model.UserModel
import com.example.tasksapp.domain.model.WorkSpaceModel
import com.example.tasksapp.presentation.commonComponents.SetTaskStatusDialogState
import com.example.tasksapp.util.TaskStatus
import java.time.LocalDateTime

data class WorkSpaceDetailState(
    val myLogin:String = "",
    val workspaceDetail: WorkSpaceModel = WorkSpaceModel("", "", "", ""),
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
    val error:String = "",
    val addTaskDialogState: AddTaskDialogState = AddTaskDialogState(),
    val addUserDialogState: AddUserDialogState = AddUserDialogState(),
    val setTaskStatusDialogState: SetTaskStatusDialogState = SetTaskStatusDialogState(),
    val tasksState: TasksState = TasksState(),
    val usersState:UsersState = UsersState(),
)

data class TasksState(
    val isSuccess:Boolean = false,
    val error:String = "",
    val isLoading:Boolean = false,
    val tasks:List<TaskModel> = emptyList(),
    val filteredTasks:List<TaskModel> = tasks,
    val selectedTasksFilter: String = TaskStatus.ALL_TASKS,
)

data class UsersState(
    val isSuccess:Boolean = false,
    val error:String = "",
    val isLoading:Boolean = false,
    val users:List<UserModel> = emptyList(),
)

data class AddTaskDialogState(
    val isOpen:Boolean = false,
    val name:String = "",
    val description:String = "",
    val error:String = "",
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
    val deadLine: LocalDateTime = LocalDateTime.MIN,
    val selectedUsers:List<String> = emptyList(),
    val users:List<UserModel> = emptyList(),

)

data class AddUserDialogState(
    val isOpen:Boolean = false,
    val userLogin:String = "",
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
    val error:String = "",
)





