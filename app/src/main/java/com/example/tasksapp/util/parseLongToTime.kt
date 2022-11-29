package com.example.tasksapp.util


fun parseLongToTime(long: Long): String {
    val sb = StringBuilder()

    var rest = long % (24 * 60 * 1000)
    val minutes = rest.toInt() / (60 * 1000)
    rest %= (60 * 1000)
    val seconds = rest.toInt() / (1000)
    rest %= (1000)
    val miniSeconds = rest.toInt() / (100)

    sb.apply {
        append(minutes)
        append(":")
        append(if (seconds.toString().length == 2) seconds else "0${seconds}")
        append(",")
        append(miniSeconds)
    }
    return sb.toString()
}