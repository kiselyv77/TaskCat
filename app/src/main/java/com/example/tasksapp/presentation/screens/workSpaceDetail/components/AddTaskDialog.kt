package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tasksapp.data.remote.Spec
import com.example.tasksapp.presentation.commonComponents.AvatarImage
import com.example.tasksapp.presentation.commonComponents.CustomTextField
import com.example.tasksapp.presentation.commonComponents.DataTimePickerDialog
import com.example.tasksapp.presentation.screens.workSpaceDetail.AddTaskDialogState
import com.example.tasksapp.util.getTime
import com.vanpra.composematerialdialogs.MaterialDialog
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
    onUserSelect: (user: String) -> Unit,
    state: AddTaskDialogState
) {
    val dialogDateState = rememberMaterialDialogState()
    val dialogTimeState = rememberMaterialDialogState()
    val userListDialogState = rememberMaterialDialogState()

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
                maxChar = 200,
                isError = state.error.isNotEmpty(),
                trailingIcon = { /*TODO*/ },
                onValueChange = { onDescriptionChanged(it) },
                onAction = { focusManager.clearFocus() })

            OutlinedButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { dialogDateState.show() }) {
                val dateStr = state.deadLine.toLocalDate()
                val time = getTime(state.deadLine)
                Text(
                    text = if (state.deadLine != LocalDateTime.MIN) "до $dateStr $time" else "Назначте сроки выполнения",
                    color = if (state.error.isEmpty()) Color.Unspecified else Color.Red
                )
            }
            OutlinedButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { userListDialogState.show() }) {

                Text(
                    text = if(state.selectedUsers.isEmpty())"Добавте пользователей" else "Вы и еще ${state.selectedUsers.size}",
                    color = if (state.error.isEmpty()) Color.Unspecified else Color.Red,
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

        MaterialDialog(
            backgroundColor = MaterialTheme.colors.background,
            dialogState = userListDialogState,
            buttons = {
                positiveButton(text = "Ок") {}
                negativeButton(text = "Отмена") {}
            }
        ) {
            LazyColumn(Modifier.padding(16.dp)) {
                items(state.users) { user ->
                    UserDialogItem(
                        clickable = { onUserSelect(user.login) },
                        name = user.name,
                        login = user.login,
                        isSelected = state.selectedUsers.contains(user.login)
                    )
                }
            }
        }
    }
}

@Composable
fun UserDialogItem(
    clickable: () -> Unit,
    name: String,
    login: String,
    isSelected:Boolean
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .clip(RoundedCornerShape(3))
            .clickable { clickable() }
    ) {
        Row(
            modifier = Modifier.padding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AvatarImage(
                imageUrl = "https://${Spec.BASE_URL}/getAvatar/${login}", modifier = Modifier
                    .padding(end = 16.dp)
                    .size(35.dp)
            )
            Column() {
                Text(
                    text = name,
                    fontSize = 15.sp
                )
                Text(
                    text = login,
                    fontSize = 10.sp
                )
            }
            Box(Modifier.fillMaxSize()) {
                RadioButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    selected = isSelected,
                    onClick = { clickable() }
                )
            }
        }
    }
}
