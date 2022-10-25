package com.example.tasksapp.main.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.tasksapp.presentation.screens.NavGraphs
import com.example.tasksapp.presentation.screens.destinations.DirectionDestination
import com.example.tasksapp.presentation.screens.destinations.ProfileScreenDestination
import com.example.tasksapp.presentation.screens.destinations.WorkSpacesListScreenDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.utils.isRouteOnBackStack

@Composable
fun BottomBar(
    navController: NavHostController
) {
    BottomNavigation() {
        BottomBarItem.values().forEach { destination ->
            val ifFirst = destination.direction.route ==
                    WorkSpacesListScreenDestination.route
                    && navController.currentDestination ==
                    null
            val isCurrentDestOnBackStack = navController.isRouteOnBackStack(destination.direction)

            BottomNavigationItem(
                selected = isCurrentDestOnBackStack||ifFirst,
                selectedContentColor = MaterialTheme.colors.onPrimary,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    if (isCurrentDestOnBackStack) {
                        // When we click again on a bottom bar item and it was already selected
                        // we want to pop the back stack until the initial destination of this bottom bar item
                        navController.popBackStack(destination.direction, false)
                        return@BottomNavigationItem
                    }

                    navController.navigate(destination.direction) {
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
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = { Text(destination.label) },
                alwaysShowLabel = false
            )
        }
    }
}

enum class BottomBarItem(
    val direction: DirectionDestination,
    val icon: ImageVector,
    val label: String
) {
    WorkSpaceList(WorkSpacesListScreenDestination, Icons.Default.List, "WorkSpaceList"),
    //Notifications(NotificationsScreenDestination, Icons.Default.Notifications, "Notifications"),
    Profile(ProfileScreenDestination, Icons.Default.Person, "Profile"),
}