package com.example.tasksapp.presentation.screens.start

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.screens.destinations.MainScreenDestination
import com.example.tasksapp.presentation.screens.destinations.RegistrationScreenDestination
import com.example.tasksapp.presentation.screens.destinations.StartScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@RootNavGraph(true)
@Destination()
@Composable
fun StartScreen(
    navigator: DestinationsNavigator,
    viewModel: StartViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    if(state.isLoading){
        //Почти моментально проходит загрузка
    }
    else{
        if(viewModel.state.value.isTokenValid){
            navigator.navigate(MainScreenDestination()) {
                this.popUpTo(StartScreenDestination.route) {
                    inclusive = true
                }
            }
        }
        else{
            navigator.navigate(RegistrationScreenDestination()) {
                this.popUpTo(StartScreenDestination.route) {
                    inclusive = true
                }
            }
        }
    }
}
