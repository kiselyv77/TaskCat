package com.example.tasksapp.util

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object UserStatus {
    const val ONLINE_STATUS = "ONLINE_STATUS"
    const val OFFLINE_STATUS = "OFFLINE_STATUS"

    fun getUserStatusName(type:String): String{
        return when(type) {
            UserStatus.ONLINE_STATUS -> "В сети"
            UserStatus.OFFLINE_STATUS -> "Не в сети"
            else -> {"error"}
        }
    }
    @Composable
    fun getUserStatusColor(type:String): Color {
        return when(type) {
            UserStatus.ONLINE_STATUS -> Color.Green
            UserStatus.OFFLINE_STATUS -> MaterialTheme.colors.onBackground
            else -> MaterialTheme.colors.onBackground
        }
    }
}