package com.example.tasksapp.presentation.screens.workSpaceDetail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.CustomFloatingActionButton
import com.example.tasksapp.presentation.commonComponents.CustomSnackbarHost
import com.example.tasksapp.presentation.commonComponents.TextPlaceHolder
import com.example.tasksapp.presentation.screens.destinations.MessengerScreenDestination
import com.example.tasksapp.presentation.screens.destinations.UsersListScreenDestination
import com.example.tasksapp.presentation.screens.workSpaceDetail.components.*
import com.example.tasksapp.util.TaskStatus
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun WorkSpaceDetailScreen(
    navigator: DestinationsNavigator,
    viewModel: WorkSpaceDetailViewModel = hiltViewModel(),
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
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            CustomFloatingActionButton(
                imageVector = Icons.Default.ArrowBack,
                onClick = { navigator.popBackStack() }
            )
        },
        snackbarHost = { snackbarHostState ->
            CustomSnackbarHost(snackbarHostState)
        }

    ) {
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = it.calculateBottomPadding()
                ),
            state = swipeRefreshState,
            onRefresh = { viewModel.onEvent(WorkSpaceDetailEvent.OnAllRefresh) }
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

                Card(modifier = Modifier.padding(vertical = 8.dp)) {
                    TextPlaceHolder(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        text = state.workspaceDetail.name,
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
                        text = state.workspaceDetail.description,
                        fontSize = 20.sp,
                        isPlaceholderVisible = state.isLoading || state.error.isNotEmpty(),
                        textPlaceHolderLength = 120
                    )
                }

                UsersPanel(
                    isPlaceholderVisible = state.usersState.isLoading||state.usersState.error.isNotEmpty(),
                    usersCount = state.usersState.users.size,
                    clickable = {navigator.navigate(UsersListScreenDestination(id))}
                )

                WorkSpaceControlPanel(
                    isAdmin = state.myLogin == state.workspaceDetail.creator,
                    addTask = { viewModel.onEvent(WorkSpaceDetailEvent.OpenCloseAddTaskDialog) },
                    addUser = { viewModel.onEvent(WorkSpaceDetailEvent.OpenCloseAddUserDialog) },
                    messenger = {navigator.navigate(MessengerScreenDestination(id))}
                )

                TasksInfoBlock(
                    completed = state.tasksState.tasks.filter { it.taskStatus == TaskStatus.COMPLITED_TYPE }.size,
                    inProgress = state.tasksState.tasks.filter { it.taskStatus == TaskStatus.INPROGRESS_TYPE }.size,
                    inPlan = state.tasksState.tasks.filter { it.taskStatus == TaskStatus.INPLAN_TYPE }.size,
                    overdue = state.tasksState.tasks.filter { it.taskStatus == TaskStatus.OVERDUE_TYPE }.size,
                    all = state.tasksState.tasks.size,
                    selectFilter = { viewModel.onEvent(WorkSpaceDetailEvent.SetTasksFilter(it)) },
                    selectedFilter = state.tasksState.selectedTasksFilter
                )

                Log.d("tasksState", state.tasksState.tasks.toString())
                if (state.tasksState.tasks.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .wrapContentSize()
                            .clip(shape = RoundedCornerShape(6)),
                    ) {
                        itemsIndexed(state.tasksState.filteredTasks) {index, task ->
                            ItemTask(
                                modifier = Modifier
                                    .width(screenWidth / 2)
                                    .height(screenHeight / 4)
                                    .padding(4.dp),
                                count = index+1,
                                name = task.name,
                                description = task.description,
                                onClick = { },
                                onLongClick = {viewModel.onEvent(WorkSpaceDetailEvent.OpenCloseSetTaskStatusDialog(task.id))},
                                taskStatus = TaskStatus.getTaskStatusName(task.taskStatus)
                            )
                        }
                    }
                } else if (state.tasksState.isSuccess) {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxSize()
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "Добавте первую задачу!"
                            )
                        }
                    }
                } else if (state.tasksState.isLoading) {
                    CircularProgressIndicator()
                }

            }
        }

        if (state.addTaskDialogState.isOpen) {
            AddTaskDialog(
                state = state.addTaskDialogState,
                dismiss = { viewModel.onEvent(WorkSpaceDetailEvent.OpenCloseAddTaskDialog) },
                onNameChanged = { viewModel.onEvent(WorkSpaceDetailEvent.SetTaskNameInDialog(it)) },
                onDescriptionChanged = { viewModel.onEvent(WorkSpaceDetailEvent.SetTaskDescriptionInDialog(it)) },
                addTask = { viewModel.onEvent(WorkSpaceDetailEvent.AddTask) },
            )
        }

        if(state.addUserDialogState.isOpen){
            AddUserDialog(
                state = state.addUserDialogState,
                dismiss = {viewModel.onEvent(WorkSpaceDetailEvent.OpenCloseAddUserDialog)},
                onLoginUserChanged = { viewModel.onEvent(WorkSpaceDetailEvent.SetUserLoginInDialog(it))},
                addUser = {viewModel.onEvent(WorkSpaceDetailEvent.AddUser)},
            )
        }

        if(state.setTaskStatusDialogState.isOpen){
            SetTaskStatusDialog(
                state = state.setTaskStatusDialogState,
                dismiss = {viewModel.onEvent(WorkSpaceDetailEvent.OpenCloseSetTaskStatusDialog()) },
                setTaskStatus = {viewModel.onEvent(WorkSpaceDetailEvent.SetTaskStatus)},
                radioButtonClick = { viewModel.onEvent(WorkSpaceDetailEvent.SetTaskStatusDialog(newStatus = it))}
            )
        }

        if (state.error.isNotEmpty()) {
            LaunchedEffect(scaffoldState.snackbarHostState) {
                val result = scaffoldState.snackbarHostState.showSnackbar("")
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.onEvent(WorkSpaceDetailEvent.OnAllRefresh)
                }
            }
        }

    }
}
