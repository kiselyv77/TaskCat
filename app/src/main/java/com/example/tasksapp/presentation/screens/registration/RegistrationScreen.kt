package com.example.tasksapp.presentation.screens.registration

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import com.example.tasksapp.presentation.screens.destinations.LoginScreenDestination
import com.example.tasksapp.presentation.screens.destinations.WorkSpacesListScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Destination()
fun RegistrationScreen(
    navigator: DestinationsNavigator,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
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
                value = state.name,
                label = "name",
                isError = viewModel.state.value.error.isNotEmpty(),
                trailingIcon = { CloseIconTextField { viewModel.onEvent(RegistrationEvent.SetName("")) } },
                onValueChange = { viewModel.onEvent(RegistrationEvent.SetName(it)) },
                onAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                )
            )
            CustomTextField(
                value = state.login,
                label = "login",
                isError = viewModel.state.value.error.isNotEmpty(),
                trailingIcon = { CloseIconTextField { viewModel.onEvent(RegistrationEvent.SetLogin("")) } },
                onValueChange = { viewModel.onEvent(RegistrationEvent.SetLogin(it)) },
                onAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                )
            )
            CustomTextField(
                value = state.password,
                label = "password",
                isError = viewModel.state.value.error.isNotEmpty(),
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = { CloseIconTextField { viewModel.onEvent(RegistrationEvent.SetPassword("")) } },
                onValueChange = { viewModel.onEvent(RegistrationEvent.SetPassword(it)) },
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
                    viewModel.onEvent(RegistrationEvent.Register)
                }) {
                Text(modifier = Modifier.padding(8.dp), text = "Зарегистрироватся")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(modifier = Modifier.padding(8.dp), text = "Уже есть аккаунт?")
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navigator.navigate(LoginScreenDestination()){
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
                    Text(text = "Войти")
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
                    text = "Вы успешно зарегистрировались"
                )
                navigator.clearBackStack(NavGraphs.root)
                navigator.navigate(WorkSpacesListScreenDestination())
            }
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

