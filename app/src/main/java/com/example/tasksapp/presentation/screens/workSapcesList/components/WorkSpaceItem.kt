package com.example.tasksapp.presentation.screens.workSapcesList.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun WorkSpaceItem(modifier: Modifier, name:String, description:String) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp),) {
            Text(
                text = name,
                fontSize = 30.sp
            )
            Text(
                text = description,
                fontSize = 20.sp
            )
        }
    }
}