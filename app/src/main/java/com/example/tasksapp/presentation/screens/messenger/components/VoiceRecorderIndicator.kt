package com.example.tasksapp.presentation.screens.messenger.components

import android.util.Log
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun VoiceRecorderIndicator(isRecord: Boolean, voiceRecordAmplitude:Int) {
    Log.d("voiceRecordAmplitude", voiceRecordAmplitude.toString())
    val openValue: Dp = 100.dp + voiceRecordAmplitude.dp
    val closeValue: Dp = 0.dp
    val animationSpec: AnimationSpec<Dp> = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    val size by animateDpAsState(
        targetValue = if (isRecord) openValue else closeValue,
        animationSpec = animationSpec
    )

    Box(modifier = Modifier.fillMaxSize().clipToBounds()){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(size/4, size/4)
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
        ){
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(30.dp)),
                imageVector = Icons.Default.KeyboardVoice,
                tint = Color.White,
                contentDescription = "clear text",
            )
        }
    }
}