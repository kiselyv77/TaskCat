package com.example.tasksapp.presentation.screens.workSapcesList.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun WorkSpaceItem(name: String, description: String, clicable: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(3))
            .clickable { clicable() }) {

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