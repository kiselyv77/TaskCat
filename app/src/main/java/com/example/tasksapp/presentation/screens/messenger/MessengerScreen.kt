package com.example.tasksapp.presentation.screens.messenger

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.domain.model.MessageModel
import com.example.tasksapp.presentation.commonComponents.CustomSnackbarHost
import com.example.tasksapp.presentation.commonComponents.CustomTextField
import com.example.tasksapp.presentation.commonComponents.SendIcon
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

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {

        },
        snackbarHost = { snackbarHostState ->
            CustomSnackbarHost(snackbarHostState)
        }

    ) {
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            state = swipeRefreshState,
            onRefresh = { }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),

                ) {
                MessageList(
                    myLogin = state.my.login,
                    modifier = Modifier.weight(1f),
                    messages = state.messagesList
                )


                CustomTextField(
                    value = state.inputMessage,
                    label = "Введите сообщение",
                    isError = false,
                    trailingIcon = {
                        SendIcon(
                            send = { viewModel.onEvent(MessengerEvent.Send) },
                            clear = { viewModel.onEvent(MessengerEvent.SetMessage("")) })
                    },
                    onValueChange = { viewModel.onEvent(MessengerEvent.SetMessage(it)) },
                    onAction = { viewModel.onEvent(MessengerEvent.Send) })
            }

        }
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<MessageModel>,
    myLogin: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            reverseLayout = true,
        ) {
            items(messages) {
                MessageCard(message = it, myLogin = myLogin)
            }
        }

    }
}

@Composable
fun MessageCard(message: MessageModel, myLogin: String) {
    val dateTime = LocalDateTime.parse(message.dateTime, DateTimeFormatter.ISO_DATE_TIME)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = when {
            message.sendingUser == myLogin -> Alignment.End
            else -> Alignment.Start
        },
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            shape = cardShapeFor(message, myLogin),
            backgroundColor = when {
                message.sendingUser == myLogin -> MaterialTheme.colors.primary
                else -> MaterialTheme.colors.secondary
            },
        ) {
            Column() {
                Text(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                    text = message.userName,
                    color = androidx.compose.ui.graphics.Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    text = message.text,
                    color = androidx.compose.ui.graphics.Color.White,
                    fontSize = 15.sp,
                )

            }
        }
        val hour = if (dateTime.hour.toString().length == 2) dateTime.hour else "0${dateTime.hour}"
        val minute =
            if (dateTime.minute.toString().length == 2) dateTime.minute else "0${dateTime.minute}"

        Row() {
            Text(
                text = "$hour:$minute",
                fontSize = 12.sp,
            )
            if (message.isArrived) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = ""
                )
            } else {
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
fun cardShapeFor(message: MessageModel, myLogin: String): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when {
        message.sendingUser == myLogin -> roundedCorners.copy(bottomEnd = CornerSize(0))
        else -> roundedCorners.copy(bottomStart = CornerSize(0))
    }
}








