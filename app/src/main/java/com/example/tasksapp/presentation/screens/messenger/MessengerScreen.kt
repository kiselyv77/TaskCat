package com.example.tasksapp.presentation.screens.messenger

import android.Manifest
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.data.remote.Spec
import com.example.tasksapp.domain.model.MessageModel
import com.example.tasksapp.presentation.commonComponents.AvatarImage
import com.example.tasksapp.presentation.screens.messenger.components.CustomMessageField
import com.example.tasksapp.presentation.screens.messenger.components.VoiceRecorderIndicator
import com.example.tasksapp.util.MessageTypes
import com.example.tasksapp.util.checkPermission
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
@Destination
fun MessengerScreen(
    viewModel: MessengerViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    id: String
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
        },
        snackbarHost = { snackbarHostState ->
            SnackbarHost(modifier = Modifier.padding(bottom = 80.dp), hostState = snackbarHostState)
        }

    ) {
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            state = swipeRefreshState,
            onRefresh = { viewModel.onEvent(MessengerEvent.Refresh) }
        ) {
            Column() {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        reverseLayout = true,
                    ) {
                        items(state.messagesList) { message ->
                            MessageCard(
                                message = message,
                                state = state,
                                voiceMessagePlayPause = { messageId ->
                                    viewModel.onEvent(MessengerEvent.PlayPauseVoiceMessage(messageId))
                                },
                                seekTo = {viewModel.onEvent(MessengerEvent.SeekTo(it))}
                            )
                        }
                    }

                }

                CustomMessageField(
                    value = state.inputMessage,
                    maxChar = 500,
                    isError = false,
                    onValueChange = { viewModel.onEvent(MessengerEvent.SetMessage(it)) },
                    send = { viewModel.onEvent(MessengerEvent.Send) },
                    clear = { viewModel.onEvent(MessengerEvent.SetMessage("")) },
                    startVoiceRecord = {
                        if (checkPermission(
                                Manifest.permission.RECORD_AUDIO,
                                context as Activity
                            )
                        ) {
                            viewModel.onEvent(MessengerEvent.StartVoiceRecord)
                        }
                    },
                    stopVoiceRecord = {
                        if (checkPermission(
                                Manifest.permission.RECORD_AUDIO,
                                context as Activity
                            )
                        ) {
                            viewModel.onEvent(MessengerEvent.StopVoiceRecord)
                        }
                    },
                    isVoiceRecording = state.isVoiceRecording
                )
            }
            VoiceRecorderIndicator(
                isRecord = state.isVoiceRecording,
                voiceRecordAmplitude = state.voiceRecordAmplitude,
                voiceRecordTime = state.voiceRecordTime
            )
        }
    }
    if (state.error.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            val result = scaffoldState.snackbarHostState.showSnackbar(state.error)
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.onEvent(MessengerEvent.Refresh)
            }
        }
    }
    if (state.recordError.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar("Удерживайте кнопку записи!")
        }
    }
}


@Composable
fun MessageCard(
    message: MessageModel,
    state: MessengerState,
    voiceMessagePlayPause: (messageId: String) -> Unit,
    seekTo: (progress: Float) -> Unit
) {
    val dateTime = LocalDateTime.parse(message.dateTime, DateTimeFormatter.ISO_DATE_TIME)
    val isMyMessage = message.sendingUser == state.my.login
    val voiceMessagesState = state.voiceMessagesState
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = when {
            isMyMessage -> Alignment.End
            else -> Alignment.Start
        },
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            if (!isMyMessage) {
                AvatarImage(
                    imageUrl = "https://${Spec.BASE_URL}/getAvatar/${message.sendingUser}",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(45.dp)
                )
            }
            Card(
                modifier = Modifier.widthIn(max = 340.dp),
                shape = cardShapeFor(isMyMessage),
                backgroundColor = when {
                    isMyMessage -> MaterialTheme.colors.primary
                    else -> MaterialTheme.colors.secondary
                },
            ) {
                Column() {
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                        text = message.userName,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    when (message.type) {
                        MessageTypes.MESSAGE_TEXT -> {
                            Text(
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    bottom = 8.dp
                                ),
                                text = message.text,
                                color = Color.White,
                                fontSize = 15.sp,
                            )
                        }
                        MessageTypes.MESSAGE_VOICE -> {
                            val isCurrent = message.id == voiceMessagesState.currentMessageId
                            val isPlaying = message.id == voiceMessagesState.currentMessageId && voiceMessagesState.playing
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { voiceMessagePlayPause(message.id) },
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(8.dp),
                                        imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(color = when {
                                            isMyMessage -> MaterialTheme.colors.secondary
                                            else -> MaterialTheme.colors.primary
                                        })
                                    )
                                }
                                Log.d(
                                    "awfsdacddvsvsvfssbsv",
                                    state.playingVoiceMessageProgress.toString()
                                )

                                Slider(
                                    value = if (isCurrent) state.playingVoiceMessageProgress else 0F,
                                    onValueChange = { if(isCurrent) seekTo(it) },
                                    colors = SliderDefaults.colors(
                                        thumbColor = Color(0xFFB71C1C),
                                        activeTrackColor = Color(0xFFEF9A9A),
                                        inactiveTrackColor = Color(0xFFFFEBEE),
                                        inactiveTickColor = Color(0xFFEF9A9A),
                                        activeTickColor = Color(0xFFB71C1C)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        val hour = if (dateTime.hour.toString().length == 2) dateTime.hour else "0${dateTime.hour}"
        val minute =
            if (dateTime.minute.toString().length == 2) dateTime.minute else "0${dateTime.minute}"

        Row() {
            Text(
                text = "$hour:$minute",
                fontSize = 10.sp,
            )
            if (message.isArrived && isMyMessage) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = ""
                )
            } else if (isMyMessage) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = ""
                )
            }
        }
    }
}


@Composable
fun cardShapeFor(isMyMessage: Boolean): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when {
        isMyMessage -> roundedCorners.copy(bottomEnd = CornerSize(0))
        else -> roundedCorners.copy(bottomStart = CornerSize(0))
    }
}








