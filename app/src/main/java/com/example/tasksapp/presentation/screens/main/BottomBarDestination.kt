package com.example.tasksapp.presentation.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.tasksapp.presentation.screens.destinations.DirectionDestination
import com.example.tasksapp.presentation.screens.destinations.ProfileScreenDestination
import com.example.tasksapp.presentation.screens.destinations.WorkSpacesListDestination

enum class BottomBarDestination(
    val direction: DirectionDestination,
    val icon: ImageVector,
    val label: String
) {
    WorkSpaces(WorkSpacesListDestination, Icons.Default.List, "WorkSpaces"),
    Profile(ProfileScreenDestination, Icons.Default.Person, "Profile") ,
}