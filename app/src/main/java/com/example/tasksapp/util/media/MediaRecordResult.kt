package com.example.tasksapp.util.media

sealed class MediaRecordResult<InputStream>(val stream: InputStream?, val fileName:String?) {
    class RecordSuccess<InputStream>(stream: InputStream?, fileName:String?):MediaRecordResult<InputStream>(stream, fileName)
    class RecordError<InputStream>(): MediaRecordResult<InputStream>(null, null)
}