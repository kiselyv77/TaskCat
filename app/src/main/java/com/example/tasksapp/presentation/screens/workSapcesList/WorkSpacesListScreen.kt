package com.example.tasksapp.presentation.screens.workSapcesList

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.CustomFloatingActionButton
import com.example.tasksapp.presentation.commonComponents.CustomSnackbarHost
import com.example.tasksapp.presentation.screens.destinations.AddWorkSpaceScreenDestination
import com.example.tasksapp.presentation.screens.destinations.WorkSpaceDetailScreenDestination
import com.example.tasksapp.presentation.screens.workSapcesList.components.WorkSpaceItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination(start = true)
@Composable
fun WorkSpacesListScreen(
    viewModel: WorkSpacesListViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<WorkSpaceDetailScreenDestination, Boolean>

) {
    Log.d("sdmvdlkdlkvmdlkvm", viewModel.hashCode().toString())
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Value -> {
                if(result.value){
                    viewModel.onEvent(WorkSpacesListEvent.OnRefresh)
                }
            }
            NavResult.Canceled -> {}
        }
    }


    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { snackbarHostState ->
            CustomSnackbarHost(
                snackbarHostState = snackbarHostState
            )
        },
        floatingActionButton = {
            CustomFloatingActionButton(
                imageVector = Icons.Default.Add,
                onClick = {
                    navigator.navigate(
                        onlyIfResumed = true,
                        direction = AddWorkSpaceScreenDestination
                    )
                }
            )
        }
    ) {
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                ),
            state = swipeRefreshState,
            onRefresh = { viewModel.onEvent(WorkSpacesListEvent.OnRefresh) }
        ) {
            if (state.workSpacesList.isEmpty() && !state.isLoading && state.error.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        text = "Добавте свое рабочее пространство или дождитесь когда вас пригласят"
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(state.workSpacesList) { workspace ->
                    WorkSpaceItem(
                        name = workspace.name,
                        description = workspace.description,
                        clicable = {
                            navigator.navigate(
                                onlyIfResumed = true,
                                direction = WorkSpaceDetailScreenDestination(workspace.id)
                            )
                        }
                    )
                }
            }
        }
    }

    if (state.error.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            val result = scaffoldState.snackbarHostState.showSnackbar("")
            if (result == SnackbarResult.ActionPerformed) viewModel.onEvent(WorkSpacesListEvent.OnRefresh)
        }
    }
}


