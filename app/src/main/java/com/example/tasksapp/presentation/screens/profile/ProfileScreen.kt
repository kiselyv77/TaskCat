package com.example.tasksapp.presentation.screens.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.tasksapp.presentation.screens.destinations.LoginScreenDestination
import com.example.tasksapp.presentation.screens.destinations.MainScreenDestination
import com.example.tasksapp.presentation.screens.destinations.RegistrationScreenDestination
import com.example.tasksapp.presentation.screens.main.BottomBarDestination
import com.example.tasksapp.presentation.screens.registration.RegistrationViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination()
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { snackbarHostState->
            SnackbarHost(
                modifier=Modifier.padding(16.dp),
                hostState = snackbarHostState
            ) { data ->
                Snackbar(
                    backgroundColor = White,
                    contentColor = MaterialTheme.colors.error,
                    action = {
                        TextButton(onClick = { data.performAction() }) {
                            Text(
                                color = Black,
                                text = "Повторить",
                            )
                        }
                    }
                ) {
                    Text(
                        text = "Ошибка загрузки",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.error,

                        )
                }
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.onEvent(ProfileEvent.Refresh) }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
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
                    Text(
                        text = state.name,
                        fontSize = 30.sp
                    )
                    Text(
                        text = state.login,
                        fontSize = 30.sp
                    )

                }
            }
            if (state.error.isNotEmpty()) {
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    val result = scaffoldState.snackbarHostState.showSnackbar("")
                    if (result == SnackbarResult.ActionPerformed) viewModel.onEvent(ProfileEvent.Refresh)
                }
            }
        }

    }

}