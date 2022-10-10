package com.example.tasksapp.presentation.screens.workSpaceDetail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.tasksapp.presentation.screens.workSpaceDetail.components.CustomAlertDialog
import com.example.tasksapp.presentation.screens.workSpaceDetail.components.ItemTask
import com.example.tasksapp.presentation.screens.workSpaceDetail.components.TasksInfo
import com.example.tasksapp.presentation.screens.workSpaceDetail.components.WorkSpaceControlPanel
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
            onRefresh = { viewModel.onEvent(WorkSpaceDetailEvent.OnRefresh) }
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
                        textPlaceHolderLength = 200
                    )
                }
                WorkSpaceControlPanel(addTask = {viewModel.onEvent(WorkSpaceDetailEvent.OpenCloseDialog)})
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column() {
                        TasksInfo("Всего задач", "10")
                        TasksInfo("Всего задач", "10")
                        TasksInfo("Всего задач", "10")
                        TasksInfo("Всего задач", "10")
                    }
                }
                Log.d("tasksState", state.tasksState.isSuccess.toString())
                if (state.tasksState.tasks.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .wrapContentSize()
                            .clip(shape = RoundedCornerShape(6)),
                    ) {
                        items(state.tasksState.tasks) { task ->
                            ItemTask(
                                modifier = Modifier
                                    .width(screenWidth / 2)
                                    .padding(4.dp),
                                count = 0,
                                name = task.name,
                                description = task.description,
                                isPlaceholdersVisible = false
                            )
                        }
                    }
                } else if(state.tasksState.isSuccess) {
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
                }else if(state.tasksState.isLoading) {
                    CircularProgressIndicator()
                }

            }
        }

        if(state.dialogState.isOpen) {
            CustomAlertDialog(
                state = state.dialogState,
                dismiss = {viewModel.onEvent(WorkSpaceDetailEvent.OpenCloseDialog)},
                onNameChanged = {viewModel.onEvent(WorkSpaceDetailEvent.SetTaskNameInDialog(it))},
                onDescriptionChanged = {viewModel.onEvent(WorkSpaceDetailEvent.SetTaskDescriptionInDialog(it))},
                addTask = {viewModel.onEvent(WorkSpaceDetailEvent.AddTask)},
            )
        }

        if (state.error.isNotEmpty()) {
            LaunchedEffect(scaffoldState.snackbarHostState) {
                val result = scaffoldState.snackbarHostState.showSnackbar("")
                if (result == SnackbarResult.ActionPerformed) { }
            }
        }
    }
}
