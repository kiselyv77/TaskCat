package com.example.tasksapp.presentation.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.CloseIconTextField
import com.example.tasksapp.presentation.commonComponents.CustomTextField
import com.example.tasksapp.presentation.screens.NavGraphs
import com.example.tasksapp.presentation.screens.destinations.RegistrationScreenDestination
import com.example.tasksapp.presentation.screens.destinations.WorkSpacesListScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo


@Composable
@Destination()
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
        CustomTextField(
            value = state.login,
            label = "логин",
            isError = viewModel.state.value.error.isNotEmpty(),
            trailingIcon = { CloseIconTextField { viewModel.onEvent(LoginEvent.SetLogin("")) } },
            onValueChange = { viewModel.onEvent(LoginEvent.SetLogin(it)) },
            onAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            )
        )
        CustomTextField(
            value = state.password,
            label = "пароль",
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
                    navigator.navigate(RegistrationScreenDestination()){
                        // Pop up to the root of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(NavGraphs.root) {
                            saveState = true
                        }

                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
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
            navigator.clearBackStack(NavGraphs.root)
            navigator.navigate(WorkSpacesListScreenDestination())
        }
        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}