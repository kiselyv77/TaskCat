package com.example.tasksapp.presentation.screens.workSapcesList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.tasksapp.presentation.screens.destinations.AddWorkSpaceScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun WorkSpacesList(
    navigator: DestinationsNavigator
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Чтобы начать работу добавте свое первое рабочее пространство!",
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
            OutlinedButton(onClick = { navigator.navigate(AddWorkSpaceScreenDestination) }) {
                Text("Добавить")
            }
        }
    }


//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        items(100){
//            Card(Modifier.fillMaxWidth()){
//                Text(modifier = Modifier.padding(16.dp), text = "Элемент списка $it", fontSize = 30.sp)
//            }
//        }
//    }
}