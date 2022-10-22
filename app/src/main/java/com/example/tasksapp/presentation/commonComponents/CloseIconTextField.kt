package com.example.tasksapp.presentation.commonComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun CloseIconTextField(clickable:()->Unit) {
    Icon(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .clickable {
                clickable()
            },
        imageVector = Icons.Default.Clear,
        contentDescription = "clear text",
    )
}


