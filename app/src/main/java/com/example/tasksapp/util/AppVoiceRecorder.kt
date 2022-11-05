package com.example.tasksapp.util

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import java.io.File

class AppVoiceRecorder(context: Context) {
    private val mediaRecorder = MediaRecorder()
    private var file = File(context.filesDir, "dsfsdvsdv")

    fun startRecord() {
        try {
            prepareMediaRecorder()
            mediaRecorder.start()
        } catch (e: Exception) {
            Log.d("AppVoiceRecorder", e.message.toString())
        }
    }

    private fun prepareMediaRecorder() {
        mediaRecorder.reset()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
        mediaRecorder.setOutputFile(file.absolutePath)
        mediaRecorder.prepare()
    }


    fun stopRecord(onSuccess: (file: File) -> Unit) {
        try {
            mediaRecorder.stop()
            onSuccess(file)
        } catch (e: Exception) {
            file.delete()
        }
    }

    fun releaseRecorder() {
        try {
            mediaRecorder.release()
        } catch (e: Exception) {
            Log.d("AppVoiceRecorder", e.message.toString())
        }
    }
}