package com.example.tasksapp.presentation.screens.messenger.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun VoiceRecorderIndicator(isRecord: Boolean) {
    val openValue: Dp = 100.dp
    val closeValue: Dp = 0.0.dp
    val animationSpec: AnimationSpec<Dp> = tween(durationMillis = 100, easing = FastOutSlowInEasing)
    val size by animateDpAsState(
        targetValue = if (isRecord) openValue else closeValue,
        animationSpec = animationSpec
    )
    Box(contentAlignment = Alignment.Center){
        Box(
            modifier = Modifier.size(size).clip(CircleShape).background(Color.Red)
        )
    }
}