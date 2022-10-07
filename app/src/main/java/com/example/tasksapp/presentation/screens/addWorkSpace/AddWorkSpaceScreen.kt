package com.example.tasksapp.presentation.screens.addWorkSpace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.CloseIconTextField
import com.example.tasksapp.presentation.commonComponents.CustomFloatingActionButton
import com.example.tasksapp.presentation.commonComponents.CustomTextField
import com.example.tasksapp.presentation.screens.destinations.AddWorkSpaceScreenDestination
import com.example.tasksapp.presentation.screens.destinations.WorkSpaceDetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination()
fun AddWorkSpaceScreen(
    navigator: DestinationsNavigator,
    viewModel: AddWorkSpaceViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            CustomFloatingActionButton(
                imageVector = Icons.Default.ArrowBack,
                onClick = { navigator.popBackStack() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CustomTextField(
                value = state.name,
                label = "name",
                isError = viewModel.state.value.error.isNotEmpty(),
                trailingIcon = { CloseIconTextField { viewModel.onEvent(AddWorkSpaceEvent.SetName("")) } },
                onValueChange = { viewModel.onEvent(AddWorkSpaceEvent.SetName(it)) },
                onAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                )
            )

            CustomTextField(
                modifier = Modifier.defaultMinSize(minHeight = 200.dp),
                value = state.description,
                singleLine = false,
                maxChar = 500,
                label = "description",
                isError = viewModel.state.value.error.isNotEmpty(),
                trailingIcon = { CloseIconTextField { viewModel.onEvent(AddWorkSpaceEvent.SetDescription("")) } },
                onValueChange = { viewModel.onEvent(AddWorkSpaceEvent.SetDescription(it)) },
                onAction = { focusManager.clearFocus() },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                )
            )

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { viewModel.onEvent(AddWorkSpaceEvent.AddWorkspace) }
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Добавить"
                )
            }

            if(state.isSuccess){
                navigator.navigate(WorkSpaceDetailScreenDestination){
                    //Удаление экранов регистрации и входа из бэк стэка
                    this.popUpTo(AddWorkSpaceScreenDestination.route) {
                        inclusive = true
                    }
                }
            }

            if (state.error.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.error,
                    text = state.error
                )
            }
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }

}









