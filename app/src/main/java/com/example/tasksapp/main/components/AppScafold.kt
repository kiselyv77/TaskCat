package com.example.tasksapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
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
    val destination =
        navController.appCurrentDestinationAsState().value ?: startRoute.startAppDestination
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
            topBar = {
                TopAppBar() {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = getScreenTitle(destination.route),
                        fontSize = 22.sp
                    )
                    if (destination.route == MessengerScreenDestination.route) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Image(
                                    modifier = Modifier.wrapContentSize().size(45.dp).padding(8.dp),
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "",
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = { bottomBar(destination) },
            content = content
        )
    }
}

private fun getScreenTitle(route: String): String {
    return when (route) {
        WorkSpacesListScreenDestination.route -> "Список рабочих пространств"
        ProfileScreenDestination.route -> "Профиль"
        LoginScreenDestination.route -> "Войти"
        RegistrationScreenDestination.route -> "Зарегистрироватся"
        AddWorkSpaceScreenDestination.route -> "Добавить рабочее пространство"
        WorkSpaceDetailScreenDestination.route -> "Рабочее пространство"
        UsersListScreenDestination.route -> "Список пользователей"
        MessengerScreenDestination.route -> "Чат"
        TaskDetailScreenDestination.route -> "Задача"
        else -> ""
    }
}

private fun ArrayDeque<NavBackStackEntry>.print(prefix: String = "stack") {
    val stack = map { it.destination.route }.toTypedArray().contentToString()
    println("$prefix = $stack")
}
