package com.example.tasksapp.presentation.commonComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SendIcon(send:()->Unit, clear:()->Unit) {
    Row() {
        Icon(
            modifier = Modifier.size(50.dp).padding(end = 8.dp)
                .clip(RoundedCornerShape(30.dp))
                .clickable {
                    clear()
                },
            imageVector = Icons.Default.Clear,
            contentDescription = "clear text",
        )
        Icon(
            modifier = Modifier.size(50.dp)
                .clip(RoundedCornerShape(30.dp))
                .clickable {
                    send()
                },
            imageVector = Icons.Default.Send,
            contentDescription = "clear text",
        )
    }

}