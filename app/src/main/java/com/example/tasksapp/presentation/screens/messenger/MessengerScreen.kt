package com.example.tasksapp.presentation.screens.messenger

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.data.remote.dto.MessageDTO
import com.example.tasksapp.presentation.commonComponents.CustomSnackbarHost
import com.example.tasksapp.presentation.commonComponents.CustomTextField
import com.example.tasksapp.presentation.commonComponents.SendIcon
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
@Destination
fun MessengerScreen(
    viewModel: MessengerViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {

        },
        snackbarHost = { snackbarHostState ->
            CustomSnackbarHost(snackbarHostState)
        }

    ) {
        Column(Modifier.fillMaxSize().padding(it)) {
            MessageList(
                myLogin = state.myLogin,
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

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<MessageDTO>,
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
fun MessageCard(message: MessageDTO, myLogin: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = when {
            message.userLogin == myLogin -> Alignment.End
            else -> Alignment.Start
        },
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            shape = cardShapeFor(message, myLogin),
            backgroundColor = when {
                message.userLogin == myLogin -> MaterialTheme.colors.primary
                else -> MaterialTheme.colors.secondary
            },
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = message.text,
                color = androidx.compose.ui.graphics.Color.White
            )
        }
        Text(
            text = message.userName,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun cardShapeFor(message: MessageDTO, myLogin: String): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when {
        message.userLogin == myLogin -> roundedCorners.copy(bottomEnd = CornerSize(0))
        else -> roundedCorners.copy(bottomStart = CornerSize(0))
    }
}








