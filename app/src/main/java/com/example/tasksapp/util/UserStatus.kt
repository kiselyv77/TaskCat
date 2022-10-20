package com.example.tasksapp.util

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
}