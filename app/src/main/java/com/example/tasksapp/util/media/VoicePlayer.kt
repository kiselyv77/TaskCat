package com.example.tasksapp.util.media

import kotlinx.coroutines.flow.Flow

interface VoicePlayer {

    suspend fun play(url: String): Flow<Float>

    suspend fun pause()

    suspend fun stop()
}