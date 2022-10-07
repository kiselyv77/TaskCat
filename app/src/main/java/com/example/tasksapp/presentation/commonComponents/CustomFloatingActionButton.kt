package com.example.tasksapp.presentation.commonComponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomFloatingActionButton(
    imageVector: ImageVector,
    onClick:()->Unit
){
    FloatingActionButton(
        modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
        onClick = { onClick() },
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "clear text",
        )
    }
}