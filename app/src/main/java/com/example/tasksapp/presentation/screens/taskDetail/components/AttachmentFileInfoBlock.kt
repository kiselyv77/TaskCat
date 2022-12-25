package com.example.tasksapp.presentation.screens.taskDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AttachmentFileInfoBlock(
    fileName: String,
    detachFile: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize().shadow(elevation = 5.dp)
            .background(color = MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.padding(16.dp).weight(0.5f),
            text = fileName,
            fontSize = 17.sp,
            maxLines = 1,
            color = MaterialTheme.colors.onPrimary
        )
        Box(){
            Icon(
                modifier = Modifier.align(Alignment.CenterEnd)
                    .size(50.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .clickable { detachFile() },
                imageVector = Icons.Default.Close,
                contentDescription = "AttachCloseFile",
                tint = MaterialTheme.colors.onPrimary
            )
        }

    }
}