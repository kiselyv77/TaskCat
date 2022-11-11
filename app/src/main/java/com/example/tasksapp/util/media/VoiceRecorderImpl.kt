package com.example.tasksapp.util.media

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
        mediaRecorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setAudioEncodingBitRate(16)
            setAudioSamplingRate(44100)
            setAudioEncodingBitRate(44100*16)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }
        isRecording = true
    }

    override suspend fun stopRecord(): File {
        if (isRecording) {
            mediaRecorder.stop()
            isRecording = false
        }
        return file
    }
}