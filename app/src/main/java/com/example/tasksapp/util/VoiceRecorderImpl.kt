package com.example.tasksapp.util

import android.app.Application
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class VoiceRecorderImpl(private val application: Application) : VoiceRecorder {
    private val mediaRecorder = MediaRecorder()
    private lateinit var file: File


    private var isRecording = false

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
        isRecording = true
    }

    override suspend fun stopRecord(): File {
        if(isRecording){
            mediaRecorder.stop()
            isRecording = false
        }
        return file
    }
}