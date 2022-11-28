package com.example.tasksapp.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getIsoDateTime(): String {
    return LocalDateTime.now()
        .format(DateTimeFormatter.ISO_DATE_TIME)
}