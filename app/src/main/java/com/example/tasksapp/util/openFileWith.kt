package com.example.tasksapp.util

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.net.toUri
import java.io.File


fun openFileWith(fileName: String, context: Context) {
    val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
    val directory = File("$root/Taskcat")
    val file = File(directory, fileName)

    // Get URI and MIME type of file
    val uri = file.toUri()
    val mime: String? = context.contentResolver.getType(uri)

    // Open file with user selected app
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.setDataAndType(uri, mime)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)
}