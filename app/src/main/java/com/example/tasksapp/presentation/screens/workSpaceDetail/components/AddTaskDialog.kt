package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tasksapp.presentation.commonComponents.CustomTextField
import com.example.tasksapp.presentation.screens.workSpaceDetail.AddTaskDialogState


@Composable
fun AddTaskDialog(
    dismiss: () -> Unit,
    update: () -> Unit,
    onNameChanged: (newName: String) -> Unit,
    onDescriptionChanged: (newDescription: String) -> Unit,
    addTask: () -> Unit,
    state: AddTaskDialogState
) {
    val focusManager = LocalFocusManager.current
    if(state.isSuccess){
        onNameChanged("")
        onDescriptionChanged("")
        dismiss()
        update()
    }
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
            CustomTextField(
                modifier = Modifier,
                value = state.name,
                label = "name",
                isError = state.error.isNotEmpty(),
                trailingIcon = { /*TODO*/ },
                onValueChange = { onNameChanged(it) },
                onAction = { focusManager.moveFocus(FocusDirection.Down) })
            CustomTextField(
                value = state.description,
                label = "description",
                isError = state.error.isNotEmpty(),
                trailingIcon = { /*TODO*/ },
                onValueChange = { onDescriptionChanged(it) },
                onAction = { focusManager.clearFocus() })

            // Buttons
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                if(state.isLoading){
                    CircularProgressIndicator()
                }
                OutlinedButton(
                    modifier = Modifier.padding(end = 16.dp, start = 16.dp, bottom = 8.dp),
                    onClick = { dismiss() }) {
                    Text(text = "Отмена")
                }
                OutlinedButton(
                    modifier = Modifier.padding(end = 16.dp, start = 16.dp, bottom = 8.dp),
                    onClick = {
                        addTask()
                    }) {
                    Text(text = "Добавить")
                }
            }
        }
    }
}
