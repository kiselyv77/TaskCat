package com.example.tasksapp.presentation.screens.taskDetail

sealed class TaskDetailEvent {
    object OnAllRefresh : TaskDetailEvent()
}
