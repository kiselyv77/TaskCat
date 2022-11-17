package com.example.tasksapp.util.media

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow

class VoicePlayerImpl(application: Application) : VoicePlayer {
    private lateinit var job: Job
    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    override suspend fun play(url: String) = flow<Float> {
        mediaPlayer.apply {
            reset()
            setDataSource(url)
            prepare() // might take long! (for buffering, etc)
            start()
            setOnCompletionListener {
                job.cancel()
                //setProgress(0f)
            }
        }
        job = CoroutineScope(Dispatchers.Main).launch{
            while(mediaPlayer.isPlaying){
                val progress = mediaPlayer.currentPosition.toFloat()/mediaPlayer.duration.toFloat()
                emit(progress)
                delay(10)
            }
        }
    }

    override suspend fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override suspend fun stop() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}