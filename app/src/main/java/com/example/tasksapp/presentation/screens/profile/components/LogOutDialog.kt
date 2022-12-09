package com.example.tasksapp.presentation.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LogOutDialog(
    dismiss: () -> Unit,
    logOut: () -> Unit
) {
    Dialog(
        properties = DialogProperties(),
        onDismissRequest = {
            dismiss()
        },
    ) {
        Column(
            Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = Color.White)
        ) {
            OutlinedButton(
                modifier = Modifier.padding(end = 16.dp, start = 16.dp, bottom = 8.dp),
                onClick = { dismiss() }) {
                Text(text = "Да")
            }
            OutlinedButton(
                modifier = Modifier.padding(end = 16.dp, start = 16.dp, bottom = 8.dp),
                onClick = { dismiss() }) {
                Text(text = "Отмена")
            }
        }
    }
}