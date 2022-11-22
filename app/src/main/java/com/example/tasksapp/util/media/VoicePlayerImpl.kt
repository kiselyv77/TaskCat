package com.example.tasksapp.util.media

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.flow.flow

class VoicePlayerImpl(application: Application) : VoicePlayer {
    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }
    private var currentUrl = ""

    override suspend fun play(url: String) = flow<Float> {
        try {
            mediaPlayer.apply {
                if(currentUrl != url){
                    reset()
                    setDataSource(url)
                    currentUrl = url
                    prepare() // might take long! (for buffering, etc)
                }
                start()
            }
            while (mediaPlayer.isPlaying) {
                val progress = mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration.toFloat()
                emit(progress)
            }
        } catch (e: Exception) {
            Log.e("VoicePlayerImpl", e.message.toString())
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

    override suspend fun seekTo(progress: Float) {
        val procent = (progress*100).toInt()
        if (procent == 0) return
        val msec = mediaPlayer.duration*procent/100
        mediaPlayer.seekTo(msec)
    }
}