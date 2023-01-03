package com.example.tasksapp.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File


fun openFileWith(fileName: String, context: Context) {
    val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
    val directory = File("$root/Taskcat")
    val file = File(directory, fileName)

    // Get URI and MIME type of file
    // Get URI and MIME type of file
    val uri: Uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
    val mime: String? = context.contentResolver.getType(uri)

    // Open file with user selected app

    // Open file with user selected app
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.setDataAndType(uri, mime)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)

}