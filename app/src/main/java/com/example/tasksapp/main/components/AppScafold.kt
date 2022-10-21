package com.example.tasksapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.plusAssign
import com.example.tasksapp.presentation.screens.appCurrentDestinationAsState
import com.example.tasksapp.presentation.screens.destinations.*
import com.example.tasksapp.presentation.screens.startAppDestination
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.spec.Route

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun SampleScaffold(
    startRoute: Route,
    navController: NavHostController,
    bottomBar: @Composable (Destination) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val destination = navController.appCurrentDestinationAsState().value ?: startRoute.startAppDestination
    val navBackStackEntry = navController.currentBackStackEntry

    // 👇 only for debugging, you shouldn't use backQueue API as it is restricted by annotation
    navController.backQueue.print()

    val bottomSheetNavigator = rememberBottomSheetNavigator()
    navController.navigatorProvider += bottomSheetNavigator

    // 👇 ModalBottomSheetLayout is only needed if some destination is bottom sheet styled
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(16.dp)
    ) {
        Scaffold(
            topBar = { TopAppBar() { Text(modifier = Modifier.padding(start = 16.dp), text = getScreenTitle(destination.route) , fontSize = 22.sp)}},
            bottomBar = { bottomBar(destination) },
            content = content
        )
    }
}

private fun getScreenTitle(route: String):String{
    return when (route){
        WorkSpacesListDestination.route -> "Список рабочих пространств"
        ProfileScreenDestination.route -> "Профиль"
        LoginScreenDestination.route -> "Войти"
        RegistrationScreenDestination.route -> "Зарегистрироватся"
        AddWorkSpaceScreenDestination.route -> "Добавить рабочее пространство"
        WorkSpaceDetailScreenDestination.route -> "Рабочее пространство"
        UsersListScreenDestination.route -> "Список пользователей"
        MessengerScreenDestination.route -> "Чат"
        else -> ""
    }
}

private fun ArrayDeque<NavBackStackEntry>.print(prefix: String = "stack") {
    val stack = map { it.destination.route }.toTypedArray().contentToString()
    println("$prefix = $stack")
}
