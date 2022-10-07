package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.presentation.commonComponents.TextPlaceHolder

@Composable
fun TasksInfo(label:String, value:String){
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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