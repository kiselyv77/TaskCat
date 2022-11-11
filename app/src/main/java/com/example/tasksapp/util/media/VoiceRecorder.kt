package com.example.tasksapp.util.media

import java.io.File

interface VoiceRecorder {

    suspend fun startRecord(messageId: String)

    suspend fun stopRecord(): File

}