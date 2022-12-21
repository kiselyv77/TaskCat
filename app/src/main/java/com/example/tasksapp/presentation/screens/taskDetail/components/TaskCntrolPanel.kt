package com.example.tasksapp.presentation.screens.taskDetail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TaskControlPanel(
    isCreator: Boolean = false,
    openDialogSetTaskStatus: () -> Unit,
    openDialogSetTaskDeadLine: () -> Unit,
    deleteTask: () -> Unit,
    leaveFromTask: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 4.dp),
                    onClick = { openDialogSetTaskStatus() }
                ) {
                    Text("Изменить статус")
                }
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    onClick = { openDialogSetTaskDeadLine() },
                    enabled = isCreator
                ) {
                    Text("Изменить сроки")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 4.dp),
                    onClick = { deleteTask() },
                    enabled = isCreator
                ) {
                    val textColor = if(isCreator) Color.Red else Color.Gray
                    Text("Удалить задачу", color = textColor)
                }
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    onClick = { leaveFromTask() },
                ) {
                    Text("Покинуть задачу")
                }
            }
        }
    }
}