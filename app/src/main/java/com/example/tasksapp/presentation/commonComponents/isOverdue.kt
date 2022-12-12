package com.example.tasksapp.presentation.commonComponents

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun isOverdue(date: String): Boolean{
    if(date == "") return false
    val deadLine = LocalDateTime.parse(
        date,
        DateTimeFormatter.ISO_DATE_TIME
    )
   return deadLine.isBefore(LocalDateTime.now()) // если срок выполнения был до сейчас
}