package com.example.tasksapp.util

import android.app.Application
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class VoiceRecorderImpl(private val application: Application) : VoiceRecorder {
    private val mediaRecorder = MediaRecorder()
    private lateinit var file: File

    override suspend fun startRecord(messageId: String) {
        file = File(application.filesDir, messageId)
        withContext(Dispatchers.IO) {
            file.createNewFile()
        }
        mediaRecorder.reset()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
        mediaRecorder.setOutputFile(file.absolutePath)
        mediaRecorder.prepare()
        mediaRecorder.start()
    }

    override suspend fun stopRecord(): File {
        mediaRecorder.stop()
        return file
    }
}