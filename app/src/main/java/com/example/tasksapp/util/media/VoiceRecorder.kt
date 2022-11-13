package com.example.tasksapp.util.media

import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface VoiceRecorder {

    suspend fun startRecord(messageId: String): Flow<MediaRecordParam>

    suspend fun stopRecord(): MediaRecordResult<InputStream>
}