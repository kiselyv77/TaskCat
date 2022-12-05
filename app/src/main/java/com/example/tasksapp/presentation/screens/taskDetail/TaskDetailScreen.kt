package com.example.tasksapp.presentation.screens.taskDetail

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.CustomFloatingActionButton
import com.example.tasksapp.presentation.commonComponents.CustomSnackbarHost
import com.example.tasksapp.presentation.commonComponents.TextPlaceHolder
import com.example.tasksapp.presentation.screens.taskDetail.components.AddInfoTextField
import com.example.tasksapp.presentation.screens.taskDetail.components.ItemNote
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


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
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            Box(modifier = Modifier.padding(bottom = 50.dp)){
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
                    modifier = Modifier.weight(1f),
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
                        }
                        items(state.notesList) { note ->
                            ItemNote(
                                userName = note.userName,
                                info = note.info,
                                dateTime = note.dateTime,
                                clicable = {}
                            )
                        }
                        item{
                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {viewModel.onEvent(TaskDetailEvent.ShowMore)}
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
                    onClear = {viewModel.onEvent(TaskDetailEvent.SetInputText(""))},
                    onSend = {viewModel.onEvent(TaskDetailEvent.SendNote)}
                )
            }
        }
    }
}