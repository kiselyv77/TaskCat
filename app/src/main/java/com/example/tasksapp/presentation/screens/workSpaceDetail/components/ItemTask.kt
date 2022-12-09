package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.presentation.commonComponents.isOverdue
import com.example.tasksapp.util.getTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemTask(
    modifier: Modifier,
    count: Int,
    name: String,
    description: String,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
    taskStatus: String,
    deadLine: String
    ) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 16.dp, end = 16.dp),
                text = "$count) $name",
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                text = description,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
            Column(){
                Text(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                    text = taskStatus,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val deadLineParsed = LocalDateTime.parse(deadLine, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                val dateStr = deadLineParsed.toLocalDate()
                val time = getTime(deadLineParsed)
                Text(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                    text = "до $dateStr $time",
                    color = if(isOverdue(deadLine)) Color.Red else Color.Unspecified,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}