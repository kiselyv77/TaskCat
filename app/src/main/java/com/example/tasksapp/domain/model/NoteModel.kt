package com.example.tasksapp.domain.model

data class NoteModel(
    val id: String,
    val info: String,
    val loginUser: String,
    val userName: String,
    val taskId: String,
    val attachmentFile: String,
    val dateTime: String,
    val isArrived: Boolean,
    val downloadState: DownLoadState = DownLoadState.NOTSAVED
){
    companion object {
        enum class DownLoadState{
            LOAD,
            SAVED,
            NOTSAVED,
            ERROR
        }
    }
}
