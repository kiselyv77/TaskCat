package com.example.tasksapp.presentation.screens.workSapcesList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun WorkSpacesList(
    navigator: DestinationsNavigator
) {
    //Хуйня со списком?!?!
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(modifier = Modifier.padding(bottom = 10.dp), text = "Ебал я рот этот джетпак компосе!", fontSize = 40.sp, textAlign = TextAlign.Center)
        Text(modifier = Modifier.padding(bottom = 10.dp), text = "Ебал я рот этот джетпак компосе!", fontSize = 40.sp, textAlign = TextAlign.Center)
        Text(modifier = Modifier.padding(bottom = 10.dp), text = "Ебал я рот этот джетпак компосе!", fontSize = 40.sp, textAlign = TextAlign.Center)
        Text(modifier = Modifier.padding(bottom = 10.dp), text = "Ебал я рот этот джетпак компосе", fontSize = 40.sp, textAlign = TextAlign.Center)
        Text(modifier = Modifier.padding(bottom = 10.dp), text = "Ебал я рот этот джетпак компосе", fontSize = 40.sp, textAlign = TextAlign.Center)
    }
}