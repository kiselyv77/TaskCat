package com.example.tasksapp.util

import java.time.LocalDateTime

fun getTime(dateTime:  LocalDateTime): String {
    val hour = if (dateTime.hour.toString().length == 2) dateTime.hour else "0${dateTime.hour}"
    val minute = if (dateTime.minute.toString().length == 2) dateTime.minute else "0${dateTime.minute}"
    return "$hour:$minute"
}