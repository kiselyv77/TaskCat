package com.example.tasksapp.presentation.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.CustomSnackbarHost
import com.example.tasksapp.presentation.commonComponents.TextPlaceHolder
import com.example.tasksapp.presentation.screens.destinations.ProfileScreenDestination
import com.example.tasksapp.presentation.screens.destinations.RegistrationScreenDestination
import com.example.tasksapp.presentation.screens.destinations.WorkSpacesListDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination()
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {

    val state = viewModel.state.value
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { snackbarHostState->
            CustomSnackbarHost(
                snackbarHostState = snackbarHostState
            )
        },
    ) {
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            state = swipeRefreshState,
            onRefresh = { viewModel.onEvent(ProfileEvent.Refresh) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, Gray, CircleShape)
                )

                TextPlaceHolder(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    text = state.name,
                    fontSize = 30.sp,
                    isPlaceholderVisible = state.isLoading || state.error.isNotEmpty()
                )
                TextPlaceHolder(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    text = state.login,
                    fontSize = 30.sp,
                    isPlaceholderVisible = state.isLoading || state.error.isNotEmpty()
                )
                TextPlaceHolder(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    text = state.status,
                    fontSize = 30.sp,
                    isPlaceholderVisible = state.isLoading || state.error.isNotEmpty()
                )


                OutlinedButton(onClick = {
                    viewModel.onEvent(ProfileEvent.LogOut)
                }) {
                    Text("Выйти из аккаунта")
                }
            }
        }


        if (state.error.isNotEmpty()) {
            LaunchedEffect(scaffoldState.snackbarHostState) {
                val result = scaffoldState.snackbarHostState.showSnackbar("")
                if (result == SnackbarResult.ActionPerformed) viewModel.onEvent(ProfileEvent.Refresh)
            }
        }
        if (state.isLogOut) {
            navigator.navigate(RegistrationScreenDestination) {
                //Удаление экранов прошлых экранов из стека
                this.popUpTo(WorkSpacesListDestination.route) {
                    inclusive = true
                }
                this.popUpTo(ProfileScreenDestination.route) {
                    inclusive = true
                }
            }
        }
    }
}



