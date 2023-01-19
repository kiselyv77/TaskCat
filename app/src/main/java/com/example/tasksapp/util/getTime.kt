package com.example.tasksapp.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun getTime(timeStamp: String): String {
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp.toLong()), ZoneId.systemDefault())

    val hour = if (dateTime.hour.toString().length == 2) dateTime.hour else "0${dateTime.hour}"
    val minute = if (dateTime.minute.toString().length == 2) dateTime.minute else "0${dateTime.minute}"
    return "$hour:$minute"
}

fun getTime(dateTime: LocalDateTime): String {
    val hour = if (dateTime.hour.toString().length == 2) dateTime.hour else "0${dateTime.hour}"
    val minute = if (dateTime.minute.toString().length == 2) dateTime.minute else "0${dateTime.minute}"
    return "$hour:$minute"
}