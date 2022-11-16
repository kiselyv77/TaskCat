package com.example.tasksapp.presentation.screens.messenger.components

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.util.parseLongToTime


@Composable
fun VoiceRecorderIndicator(isRecord: Boolean, voiceRecordAmplitude: Float, voiceRecordTime: Long) {
    Log.d("voiceRecordAmplitude", voiceRecordAmplitude.toString())
    val openValue: Dp =
        if (100.dp + voiceRecordAmplitude.dp < 200.dp) 100.dp + voiceRecordAmplitude.dp else 200.dp
    val closeValue: Dp = 1.dp
    val animationSpec: AnimationSpec<Dp> = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    val size by animateDpAsState(
        targetValue = if (isRecord) openValue else closeValue,
        animationSpec = animationSpec
    )
    val voiceRecordTimeParced = parseLongToTime(voiceRecordTime)

    val infiniteTransition = rememberInfiniteTransition()

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        if (isRecord) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 64.dp)
                        .clip(CircleShape)
                        .size(10.dp)
                        .background(color = Color.Red.copy(alpha = alpha))
                )
                Text(
                    modifier = Modifier.padding(start = 40.dp),
                    text = voiceRecordTimeParced,
                    fontSize = 16.sp,
                    fontWeight = Bold
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(size / 4, size / 4)
                .size(size)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colors.primary.copy(alpha = 0.4f),
                            MaterialTheme.colors.secondary,
                        )
                    )
                )
        ) {
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(30.dp)),
                imageVector = Icons.Default.KeyboardVoice,
                tint = MaterialTheme.colors.primary,
                contentDescription = "clear text",
            )
        }
    }
}