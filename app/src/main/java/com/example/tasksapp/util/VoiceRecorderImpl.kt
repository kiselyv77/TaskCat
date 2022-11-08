package com.example.tasksapp.util

import android.app.Application
import android.media.MediaRecorder
import java.io.File

class VoiceRecorderImpl(private val application: Application) : VoiceRecorder {

    private val mediaRecorder = MediaRecorder()
    private lateinit var file: File
    private lateinit var mMessageId: String

    override suspend fun startRecord(messageId: String) {
        mMessageId = messageId
        file = File(application.filesDir, mMessageId)
        file.createNewFile()
        prepareMediaRecorder()
        mediaRecorder.start()
    }

    override suspend fun stopRecord(): File {
        mediaRecorder.stop()
        return file
    }

    override suspend fun prepareMediaRecorder() {
        mediaRecorder.reset()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
        mediaRecorder.setOutputFile(file.absolutePath)
        mediaRecorder.prepare()
    }

    override suspend fun releaseRecorder() {
        mediaRecorder.release()
    }
}