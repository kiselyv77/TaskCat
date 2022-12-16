package com.example.tasksapp.presentation.screens.workSapcesList.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun WorkSpaceItem(name: String, description: String, clicable: () -> Unit, delete: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(3))
            .clickable { clicable() }) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(modifier = Modifier.align(Alignment.BottomEnd), onClick = { delete() }) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary)
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = name, fontSize = 30.sp
                )
                Text(
                    text = description, fontSize = 20.sp
                )
            }
        }
    }
}