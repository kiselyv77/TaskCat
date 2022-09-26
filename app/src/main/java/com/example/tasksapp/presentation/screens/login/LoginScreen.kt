package com.example.tasksapp.presentation.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.CloseIconTextField
import com.example.tasksapp.presentation.commonComponents.SingleLineTextField
import com.example.tasksapp.presentation.screens.destinations.LoginScreenDestination
import com.example.tasksapp.presentation.screens.destinations.MainScreenDestination
import com.example.tasksapp.presentation.screens.destinations.RegistrationScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SingleLineTextField(
            value = state.login,
            label = "login",
            isError = viewModel.state.value.error.isNotEmpty(),
            trailingIcon = { CloseIconTextField { viewModel.onEvent(LoginEvent.SetLogin("")) } },
            onValueChange = { viewModel.onEvent(LoginEvent.SetLogin(it)) },
            onAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            )
        )
        SingleLineTextField(
            value = state.password,
            label = "password",
            isError = viewModel.state.value.error.isNotEmpty(),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = { CloseIconTextField { viewModel.onEvent(LoginEvent.SetPassword("")) } },
            onValueChange = { viewModel.onEvent(LoginEvent.SetPassword(it)) },
            onAction = { focusManager.clearFocus() },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        )
        OutlinedButton(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = {
                viewModel.onEvent(LoginEvent.Login)
            }) {
            Text(modifier = Modifier.padding(8.dp), text = "Войти")
        }
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(modifier = Modifier.padding(8.dp), text = "Еще не зарегестированны?")
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navigator.popBackStack()
                }) {
                Text(text = "Регистрация")
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
        if (state.token.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Center,
                color = Color.Green,
                text = "Вы успешно вошли в свой аккаунт"
            )
            navigator.navigate(MainScreenDestination()) {
                //Удаление экранов регистрации и входа из бэк стэка
                this.popUpTo(LoginScreenDestination.route) {
                    inclusive = true
                }
                this.popUpTo(RegistrationScreenDestination.route) {
                    inclusive = true
                }
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}