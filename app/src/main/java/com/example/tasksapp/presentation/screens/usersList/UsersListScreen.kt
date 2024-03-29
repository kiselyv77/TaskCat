package com.example.tasksapp.presentation.screens.usersList

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.CustomFloatingActionButton
import com.example.tasksapp.presentation.commonComponents.CustomSnackbarHost
import com.example.tasksapp.presentation.commonComponents.UserItemDialog
import com.example.tasksapp.presentation.screens.usersList.components.UserItem
import com.example.tasksapp.util.UserTypes
import com.example.tasksapp.util.UserTypes.getUserTypeName
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun UsersListScreen(
    navigator: DestinationsNavigator,
    viewModel: UserListViewModel = hiltViewModel(),
    id: String
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )
    Log.d("userList", state.usersList.toString())

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { snackbarHostState ->
            CustomSnackbarHost(
                snackbarHostState = snackbarHostState
            )
        },
        floatingActionButton = {
            CustomFloatingActionButton(
                imageVector = Icons.Default.ArrowBack,
                onClick = { navigator.popBackStack() }
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
            onRefresh = { viewModel.onEvent(UserListEvent.OnRefresh) }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                // Будет true если мы создатель // только создатель может менять статус кого либо
                val isCreator = state.usersList.firstOrNull { it.login == state.myLogin }?.userStatusToWorkSpace ==
                        UserTypes.CREATOR_TYPE

                items(state.usersList) { user ->
                    UserItem(
                        name = user.name,
                        login = user.login,
                        status = user.status,
                        userStatusToWorkSpace = getUserTypeName(user.userStatusToWorkSpace),
                        clickable = if(user.userStatusToWorkSpace != UserTypes.CREATOR_TYPE && isCreator) {{
                            viewModel.onEvent(UserListEvent.CloseOpenDialog(user.login, user.userStatusToWorkSpace))
                        }} else{{
                            // Ничего не делаем
                        }}
                    )
                }
            }
        }
    }
    if(state.dialogState.isOpen){
        UserItemDialog(
            state = state.dialogState,
            dismiss = { viewModel.onEvent(UserListEvent.CloseOpenDialog()) },
            setStatus = {viewModel.onEvent(UserListEvent.SetUserStatusToWorkSpace)},
            deleteUser = { viewModel.onEvent(UserListEvent.DeleteUser) }
        )
    }
    if (state.error.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            val result = scaffoldState.snackbarHostState.showSnackbar("")
            if (result == SnackbarResult.ActionPerformed) viewModel.onEvent(UserListEvent.OnRefresh)
        }
    }
}