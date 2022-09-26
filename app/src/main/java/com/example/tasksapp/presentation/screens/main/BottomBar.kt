package com.example.tasksapp.presentation.screens.main

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.tasksapp.presentation.screens.NavGraphs
import com.example.tasksapp.presentation.screens.appCurrentDestinationAsState
import com.example.tasksapp.presentation.screens.startAppDestination

@Composable
fun BottomBar(navController: NavHostController) {

    val currentDestination  = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    BottomNavigation(backgroundColor = Color.White) {
        BottomBarDestination.values().forEach { destination ->
            BottomNavigationItem(

                selected = currentDestination.route == destination.direction.route,
                onClick = {
                    navController.navigate(destination.direction.route) {
                        launchSingleTop = true
                        this.popUpTo(currentDestination.route) {
                            inclusive = true
                        }
                    }
                },
                alwaysShowLabel = false,
                icon = { Icon(destination.icon, "")},
                label = {Text(destination.label)}
            )
        }
    }
}