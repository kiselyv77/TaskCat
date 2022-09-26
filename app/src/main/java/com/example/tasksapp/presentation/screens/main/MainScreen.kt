package com.example.tasksapp.presentation.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.tasksapp.presentation.screens.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination

@Destination()
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ){
            DestinationsNavHost(
                startRoute = BottomBarDestination.WorkSpaces.direction,
                navController = navController,
                navGraph = NavGraphs.root
            )
        }
    }
}
