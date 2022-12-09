package com.example.tasksapp.presentation.screens.taskDetail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.*
import com.example.tasksapp.presentation.screens.taskDetail.components.AddInfoTextField
import com.example.tasksapp.presentation.screens.taskDetail.components.ItemNote
import com.example.tasksapp.presentation.screens.taskDetail.components.TaskControlPanel
import com.example.tasksapp.util.TaskStatus
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
@Destination
fun TaskDetailScreen(
    navigator: DestinationsNavigator,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    id: String
) {
    val state = viewModel.state.value
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val scaffoldState = rememberScaffoldState()
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val dialogDateState = rememberMaterialDialogState()
    val dialogTimeState = rememberMaterialDialogState()
    val dateState = remember {
        mutableStateOf<LocalDate>(LocalDate.now())
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            Box(modifier = Modifier.padding(bottom = 50.dp)) {
                CustomFloatingActionButton(
                    imageVector = Icons.Default.ArrowBack,
                    onClick = { navigator.popBackStack() }
                )
            }
        },
        snackbarHost = { snackbarHostState ->
            CustomSnackbarHost(snackbarHostState)
        }

    ) {
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = it.calculateBottomPadding()
                ),
            state = swipeRefreshState,
            onRefresh = { viewModel.onEvent(TaskDetailEvent.OnAllRefresh) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        focusManager.clearFocus()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Card(modifier = Modifier.padding(vertical = 8.dp)) {
                                TextPlaceHolder(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    text = state.task.name,
                                    fontSize = 30.sp,
                                    isPlaceholderVisible = state.isLoading || state.error.isNotEmpty()
                                )
                            }
                            Card(modifier = Modifier.padding(vertical = 8.dp)) {
                                TextPlaceHolder(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .heightIn(max = screenHeight / 3),
                                    text = state.task.description,
                                    fontSize = 20.sp,
                                    isPlaceholderVisible = state.isLoading || state.error.isNotEmpty(),
                                    textPlaceHolderLength = 120
                                )
                            }

                            Card(modifier = Modifier.padding(vertical = 8.dp)) {
                                Column() {
                                    TextPlaceHolder(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                            .heightIn(max = screenHeight / 3),
                                        text = "${TaskStatus.getTaskStatusName(state.task.taskStatus)} \nдо ${state.task.deadLine}",
                                        color = if(isOverdue(state.task.deadLine)) Color.Red else Color.Unspecified,
                                        fontSize = 20.sp,
                                        isPlaceholderVisible = state.isLoading || state.error.isNotEmpty(),
                                        textPlaceHolderLength = 10
                                    )
                                }
                            }

                            TaskControlPanel(
                                openDialogSetTaskStatus = {
                                    viewModel.onEvent(TaskDetailEvent.OpenCloseSetTaskStatusDialog)
                                },
                                openDialogSetTaskDeadLine = {
                                    dialogDateState.show()
                                }
                            )

                            val firstUsers = listOf(
                                state.usersState.users.getOrNull(0)?.login,
                                state.usersState.users.getOrNull(1)?.login,
                                state.usersState.users.getOrNull(2)?.login,
                            )

                            UsersPanel(
                                firstUsers = firstUsers,
                                isPlaceholderVisible = state.usersState.isLoading||state.usersState.error.isNotEmpty(),
                                usersCount = state.usersState.users.size,
                                clickable = { Log.d("sadasdasdasd", state.usersState.users.toString())}
                            )
                        }
                        items(state.notesList) { note ->
                            ItemNote(
                                userName = note.userName,
                                info = note.info,
                                dateTime = note.dateTime,
                                clicable = {}
                            )
                        }
                        item {
                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { viewModel.onEvent(TaskDetailEvent.ShowMore) }
                            ) {
                                Text("Показать больше")
                            }
                        }
                    }
                }
                AddInfoTextField(
                    value = state.inputText,
                    label = "Введите подробности обновлений связаных с задачей",
                    isError = state.error.isNotEmpty(),
                    onValueChange = { viewModel.onEvent(TaskDetailEvent.SetInputText(it)) },
                    onClear = { viewModel.onEvent(TaskDetailEvent.SetInputText("")) },
                    onSend = { viewModel.onEvent(TaskDetailEvent.SendNote) }
                )
            }
        }

        if(state.setTaskStatusDialogState.isOpen){
            SetTaskStatusDialog(
                state = state.setTaskStatusDialogState,
                dismiss = {viewModel.onEvent(TaskDetailEvent.OpenCloseSetTaskStatusDialog) },
                setTaskStatus = {viewModel.onEvent(TaskDetailEvent.SetTaskStatus)},
                radioButtonClick = { viewModel.onEvent(TaskDetailEvent.SetTaskStatusDialog(newStatus = it))}
            )
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
                viewModel.onEvent(TaskDetailEvent.SetTaskDeadLine(dateTime))
            }
        )
    }
}