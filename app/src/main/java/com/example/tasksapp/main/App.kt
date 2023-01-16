package com.example.tasksapp.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tasksapp.SampleScaffold
import com.example.tasksapp.main.components.BottomBar
import com.example.tasksapp.presentation.screens.NavGraphs
import com.example.tasksapp.presentation.screens.destinations.*
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun App(viewModel: StartViewModel) {
    val animateDuration = 350

    val state = viewModel.state.value

    val engine = rememberAnimatedNavHostEngine(rootDefaultAnimations = RootNavGraphDefaultAnimations(
        enterTransition = { scaleIn(animationSpec = tween(animateDuration)) },
        exitTransition = { scaleOut(animationSpec = tween(animateDuration)) },
    ))

    val navController = engine.rememberNavController()

    val startRoute = if (state.isTokenValid) NavGraphs.root.startRoute else RegistrationScreenDestination

    if(!state.isLoading){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SampleScaffold(
                navController = navController,
                startRoute = startRoute,
                bottomBar = {
                    if (it.isShowBottomBar) {
                        BottomBar(navController)
                    }
                }
            ) {
                DestinationsNavHost(
                    modifier = Modifier.padding(it),
                    engine = engine,
                    navController = navController,
                    navGraph = NavGraphs.root,
                    startRoute = startRoute
                )
            }
        }
    }else{
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Text(text = "Loading...",fontSize = 50.sp)
        }
    }
}

private val Destination.isShowBottomBar
    get() = this !is RegistrationScreenDestination &&
            this !is LoginScreenDestination &&
            this !is MessengerScreenDestination &&
            this !is TaskDetailScreenDestination