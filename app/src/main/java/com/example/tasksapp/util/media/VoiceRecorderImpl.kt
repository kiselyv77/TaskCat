package com.example.tasksapp.util.media

import android.app.Application
import android.media.MediaRecorder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class VoiceRecorderImpl(private val application: Application) : VoiceRecorder {
    private val mediaRecorder = MediaRecorder()
    private lateinit var file: File

    private val LOG_TAG = "VoiceRecorderImpl"

    private var time: Long = 0

    private var isRecording = false

    override suspend fun startRecord(
        messageId: String,
    ): Flow<MediaRecordParam> = flow<MediaRecordParam> {
        try {
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
                setAudioEncodingBitRate(44100 * 16)
                setOutputFile(file.absolutePath)
                prepare()
                start()
            }
            Log.d(LOG_TAG, "RecordStart")
            isRecording = true
            while (isRecording) {
                delay(100)
                time += 100
                Log.d(LOG_TAG, MediaRecordParam(time = time, amplitude = mediaRecorder.maxAmplitude).toString())
                emit(MediaRecordParam(time = time, amplitude = mediaRecorder.maxAmplitude))
            }
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.toString())
        }

    }

    override suspend fun stopRecord(): MediaRecordResult<InputStream> {
        return try {
            Log.d(LOG_TAG,"RecordStop")
            mediaRecorder.stop()
            val fileName = file.name
            val stream: InputStream = withContext(Dispatchers.IO) { FileInputStream(file) }
            if(time==0L) {
                Log.d(LOG_TAG, "Время записи равно 0")
                file.delete()
                MediaRecordResult.RecordError<InputStream>()
            }
            else {
                Log.d(LOG_TAG, "stream и fileName отправлены во ViewModel")
                MediaRecordResult.RecordSuccess<InputStream>(stream, fileName)
            }
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.toString())
            file.delete()
            MediaRecordResult.RecordError<InputStream>()
        } finally {
            time = 0L
            isRecording = false
        }
    }
}