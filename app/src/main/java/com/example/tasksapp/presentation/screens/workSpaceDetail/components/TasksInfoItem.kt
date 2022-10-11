package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.presentation.commonComponents.TextPlaceHolder

@Composable
fun TasksInfo(modifier: Modifier, label:String, value:String){
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(modifier = Modifier.padding(end = 16.dp), text = label, fontSize = 25.sp)
        TextPlaceHolder(
            modifier = Modifier.fillMaxSize(),
            text = value,
            fontSize = 25.sp,
            isPlaceholderVisible = false
        )
    }
}


@Composable
fun TasksInfoBlock(
    completed: String,
    inProgress: String,
    inPlan: String,
    overdue: String,
    all: String,

){
    Box(contentAlignment = Alignment.Center){
        Column(modifier = Modifier.padding(vertical = 8.dp)){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Card(modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(end = 4.dp)){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(text = "В плане", fontSize = 25.sp) // В плане
                        Text(text = inPlan, fontSize = 20.sp)
                    }
                }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)){
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(text = "В процессе", fontSize = 25.sp) // В процессе
                        Text(text = inProgress, fontSize = 20.sp)
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Card(modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(end = 4.dp)){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(text = "Выполнено", fontSize = 25.sp) // Выполнено
                        Text(text = completed, fontSize = 20.sp)
                    }
                }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)){
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(text = "Долги", fontSize = 25.sp)// Долги
                        Text(text = overdue, fontSize = 20.sp)
                    }
                }

            }
        }
        Card(modifier = Modifier.size(45.dp), elevation = 2.dp){
            Box(contentAlignment = Alignment.Center) {
                Text(text = all, fontSize = 20.sp)
            }
        }
    }

}