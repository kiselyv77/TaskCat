package com.example.tasksapp.presentation.screens.taskDetail

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.*
import com.example.tasksapp.presentation.screens.taskDetail.components.*
import com.example.tasksapp.util.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
@Destination
fun TaskDetailScreen(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    id: String,
    workSpaceId: String
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

    val isCreator =
        state.usersState.users.lastOrNull { it.login == state.my.login }?.userStatusToTask == UserTypes.CREATOR_TYPE

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = getFileName(context, it)
            Log.d("fileName", fileName)
            val stream = context.contentResolver.openInputStream(it)
            stream?.let {
                viewModel.onEvent(TaskDetailEvent.AttachFile(it, fileName))
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
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
                                        color = if (isOverdue(state.task.deadLine)) Color.Red else Color.Unspecified,
                                        fontSize = 20.sp,
                                        isPlaceholderVisible = state.isLoading || state.error.isNotEmpty(),
                                        textPlaceHolderLength = 10
                                    )
                                }
                            }

                            TaskControlPanel(
                                isCreator = isCreator,
                                openDialogSetTaskStatus = {
                                    viewModel.onEvent(TaskDetailEvent.OpenCloseSetTaskStatusDialog)
                                },
                                openDialogSetTaskDeadLine = {
                                    dialogDateState.show()
                                },
                                deleteTask = {
                                    viewModel.onEvent(TaskDetailEvent.OpenCloseDeleteTaskDialog)
                                },
                                leaveFromTask = {
                                    viewModel.onEvent(TaskDetailEvent.OpenCloseLeaveFromTaskDialog)
                                }
                            )

                            UsersRow(
                                userList = state.usersState.users,
                                addUserOpenDialog = {
                                    viewModel.onEvent(TaskDetailEvent.OpenCloseAddUserToTaskDialog)
                                },
                                onUserClick = {
                                    viewModel.onEvent(
                                        TaskDetailEvent.OpenCloseUserItemDialog(
                                            userModel = it
                                        )
                                    )
                                }
                            )
                        }
                        items(state.notesList) { note ->
                            ItemNote(
                                note = note,
                                downloadFile = {
                                    if(checkPermission(WRITE_EXTERNAL_STORAGE, context as Activity)){
                                        viewModel.onEvent(TaskDetailEvent.DownloadFile(note.id + note.attachmentFile))
                                        Log.d("smflaksdksdlkfmf", "downloadFile")
                                    }
                                },
                                openFile = {
                                    openFileWith(note.attachmentFile, context)
                                    Log.d("smflaksdksdlkfmf", "openFileWith")
                                },
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
                if (state.attachmentFileInfo.attachmentFile != null) {
                    AttachmentFileInfoBlock(
                        fileName = state.attachmentFileInfo.originalFileName,
                        detachFile = {
                            viewModel.onEvent(TaskDetailEvent.DetachFile)
                        }
                    )
                }
                AddInfoTextField(
                    value = state.inputText,
                    isFileAttach = state.attachmentFileInfo.attachmentFile != null,
                    label = "Введите подробности обновлений связаных с задачей",
                    isError = state.error.isNotEmpty(),
                    onValueChange = { viewModel.onEvent(TaskDetailEvent.SetInputText(it)) },
                    onClear = { viewModel.onEvent(TaskDetailEvent.SetInputText("")) },
                    onSend = { viewModel.onEvent(TaskDetailEvent.SendNote) },
                    onPickFile = { launcher.launch("*/*") }
                )
            }
        }

        if (state.setTaskStatusDialogState.isOpen) {
            SetTaskStatusDialog(
                state = state.setTaskStatusDialogState,
                dismiss = { viewModel.onEvent(TaskDetailEvent.OpenCloseSetTaskStatusDialog) },
                setTaskStatus = { viewModel.onEvent(TaskDetailEvent.SetTaskStatus) },
                radioButtonClick = { viewModel.onEvent(TaskDetailEvent.SetTaskStatusDialog(newStatus = it)) }
            )
        }


        if (state.deleteTaskDialogState.isOpen) {
            CustomAlertDialog(
                label = "Вы действительно хотите удалить эту задачу?",
                state = state.deleteTaskDialogState,
                ok = { viewModel.onEvent(TaskDetailEvent.DeleteTask) },
                dismiss = { viewModel.onEvent(TaskDetailEvent.OpenCloseDeleteTaskDialog) },
                onSuccess = {
                    viewModel.onEvent(TaskDetailEvent.OpenCloseDeleteTaskDialog)
                    resultNavigator.navigateBack(result = true)
                }
            )
        }

        if (state.leaveFromTaskDialogState.isOpen) {
            CustomAlertDialog(
                label = "Вы действительно хотите покинуть эту задачу?",
                state = state.leaveFromTaskDialogState,
                ok = { viewModel.onEvent(TaskDetailEvent.LeaveFromTask) },
                dismiss = { viewModel.onEvent(TaskDetailEvent.OpenCloseLeaveFromTaskDialog) },
                onSuccess = {
                    viewModel.onEvent(TaskDetailEvent.OpenCloseLeaveFromTaskDialog)
                    resultNavigator.navigateBack(result = true)
                }
            )
        }

        if (state.userItemDialogState.isOpen) {
            UserItemDialog2(
                state = state.userItemDialogState,
                myLogin = state.my.login,
                isCreator = isCreator,
                onSuccess = { viewModel.onEvent(TaskDetailEvent.OpenCloseUserItemDialog()) },
                dismiss = { viewModel.onEvent(TaskDetailEvent.OpenCloseUserItemDialog()) },
                deleteUser = { viewModel.onEvent(TaskDetailEvent.DeleteUser) }
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
                Log.d("asdasdasdasdads", "SetTaskDeadLine")
                viewModel.onEvent(TaskDetailEvent.SetTaskDeadLine(dateTime))
            }
        )
        if (state.addUserDialogState.isOpen) {
            AddUserToTaskDialog(
                state = state.addUserDialogState,
                onUserSelect = { viewModel.onEvent(TaskDetailEvent.AddUserToTask(userLogin = it)) },
                dismiss = { viewModel.onEvent(TaskDetailEvent.OpenCloseAddUserToTaskDialog) },
            )
        }

    }
    if (state.error.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            val result = scaffoldState.snackbarHostState.showSnackbar("")
            if (result == SnackbarResult.ActionPerformed) viewModel.onEvent(TaskDetailEvent.OnAllRefresh)
        }
    }
}


