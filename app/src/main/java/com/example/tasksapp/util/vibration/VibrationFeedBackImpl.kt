package com.example.tasksapp.util.vibration

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class VibrationFeedBackImpl(application: Application): VibrationFeedBack {

    private val vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val canVibrate: Boolean = vibrator.hasVibrator()

    override fun startVibration(milliseconds: Long) {
        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                // This method was deprecated in API level 26
                vibrator.vibrate(milliseconds)
            }
        }
    }
}