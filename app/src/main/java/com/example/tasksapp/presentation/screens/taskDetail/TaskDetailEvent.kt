package com.example.tasksapp.presentation.screens.taskDetail

sealed class TaskDetailEvent {
    object OnAllRefresh : TaskDetailEvent()
    data class SetInputText(val newText: String): TaskDetailEvent()
    object SendNote: TaskDetailEvent()
}
