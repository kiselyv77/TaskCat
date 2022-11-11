package com.example.tasksapp.util.media

interface VoicePlayer {

    suspend fun play(url: String, setProgress: (progress: Float) -> Unit)

    suspend fun pause()

    suspend fun stop()
}