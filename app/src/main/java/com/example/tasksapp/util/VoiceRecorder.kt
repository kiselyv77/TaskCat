package com.example.tasksapp.util

import java.io.File

interface VoiceRecorder {

    suspend fun startRecord(messageId: String)

    suspend fun prepareMediaRecorder()

    suspend fun stopRecord(): File

    suspend fun releaseRecorder()
}