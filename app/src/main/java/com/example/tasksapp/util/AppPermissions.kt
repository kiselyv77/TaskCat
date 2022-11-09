package com.example.tasksapp.util

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

    const val PERMISSION_REQUIRED = 200

    fun checkPermission(permission: String, activity: Activity): Boolean{
        return if (ContextCompat.checkSelfPermission(activity, permission)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_REQUIRED)
            false
        } else true

    }
