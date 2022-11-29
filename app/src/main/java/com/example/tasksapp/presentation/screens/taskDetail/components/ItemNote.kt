package com.example.tasksapp.presentation.screens.taskDetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.util.getTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ItemNote(loginUser: String, userName:String, info: String,dateTime: String, clicable: () -> Unit) {
    val parsedDataTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
    val date = parsedDataTime.toLocalDate()
    val time = getTime(parsedDataTime)
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp).clip(RoundedCornerShape(3))
            .clickable { clicable() }
    ) {
        Column(modifier = Modifier.padding(16.dp),) {
            Text(
                text = userName,
                fontSize = 30.sp
            )
            Text(
                text = loginUser,
                fontSize = 20.sp
            )
            Text(
                text = info,
                fontSize = 20.sp
            )
            Text(
                text = "$date в $time",
                fontSize = 20.sp
            )
        }
    }
}