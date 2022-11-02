package com.example.tasksapp.presentation.screens.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tasksapp.data.remote.Spec.BASE_URL
import com.example.tasksapp.presentation.commonComponents.CustomSnackbarHost
import com.example.tasksapp.presentation.commonComponents.TextPlaceHolder
import com.example.tasksapp.presentation.screens.NavGraphs
import com.example.tasksapp.presentation.screens.destinations.RegistrationScreenDestination
import com.example.tasksapp.util.UserStatus
import com.example.tasksapp.util.UserStatus.getUserStatusName
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
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let{
            val stream = context.contentResolver.openInputStream(it)
            stream?.let{
                viewModel.onEvent(ProfileEvent.UploadNewAvatarEvent(stream))
            }
        }
    }

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

                AsyncImage(
                    model = "https://$BASE_URL/getAvatar/${state.login}?" + (0..1_000_000).random(),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, Gray, CircleShape)
                        .clickable { launcher.launch("image/*") }
                )

                TextPlaceHolder(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 4.dp
                    ),
                    text = state.name,
                    fontSize = 30.sp,
                    isPlaceholderVisible = state.isLoading || state.error.isNotEmpty()
                )
                TextPlaceHolder(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 4.dp
                    ),
                    text = state.login,
                    fontSize = 25.sp,
                    isPlaceholderVisible = state.isLoading || state.error.isNotEmpty()
                )
                TextPlaceHolder(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 4.dp
                    ),
                    text = getUserStatusName(state.status) ,
                    fontSize = 20.sp,
                    isPlaceholderVisible = state.isLoading || state.error.isNotEmpty(),
                    color = if(state.status == UserStatus.ONLINE_STATUS) Color.Green else MaterialTheme.colors.onBackground
                )

                OutlinedButton(onClick = {

                }) {
                    Text("Редактировать профиль")
                }

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
            navigator.clearBackStack(NavGraphs.root)
            navigator.navigate(RegistrationScreenDestination)
            //Наконецто
        }
    }
}



