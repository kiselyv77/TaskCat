package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tasksapp.presentation.commonComponents.CustomTextField
import com.example.tasksapp.presentation.commonComponents.DataTimePickerDialog
import com.example.tasksapp.presentation.screens.workSpaceDetail.AddTaskDialogState
import com.example.tasksapp.util.getTime
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
fun AddTaskDialog(
    dismiss: () -> Unit,
    onNameChanged: (newName: String) -> Unit,
    onDescriptionChanged: (newDescription: String) -> Unit,
    onDeadLineChanged: (deadLine: LocalDateTime) -> Unit,
    addTask: () -> Unit,
    state: AddTaskDialogState
) {
    val dialogDateState = rememberMaterialDialogState()
    val dialogTimeState = rememberMaterialDialogState()

    val dateState = remember {
        mutableStateOf<LocalDate>(LocalDate.now())
    }

    val focusManager = LocalFocusManager.current

    if (state.isSuccess) {
        onNameChanged("")
        onDescriptionChanged("")
        dismiss()
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

            OutlinedButton(
                modifier = Modifier.padding(16.dp),
                onClick = { dialogDateState.show() }) {
                val dateStr = state.deadLine.toLocalDate()
                val time = getTime(state.deadLine)
                Text(
                    text = if(state.deadLine != LocalDateTime.MIN) "до $dateStr $time" else "Назначте сроки выполнения",
                    color = if (state.error.isEmpty()) Color.Unspecified else Color.Red
                )
            }

            // Buttons
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                }
                if (state.error.isNotEmpty()) {
                    val context = LocalContext.current
                    LaunchedEffect(state.error) {
                        Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                    }
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

        DataTimePickerDialog(
            dialogDateState = dialogDateState,
            dialogTimeState = dialogTimeState,
            onDateChange = { date ->
                dateState.value = date
            },
            onTimeChange = { time ->
                val dateTime = LocalDateTime.of(
                    dateState.value,
                    time
                )
                onDeadLineChanged(dateTime)
            }
        )
    }
}
