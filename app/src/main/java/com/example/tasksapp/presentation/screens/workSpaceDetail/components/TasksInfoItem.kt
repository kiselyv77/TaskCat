package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.presentation.commonComponents.TextPlaceHolder
import com.example.tasksapp.util.TaskStatus

@Composable
fun TasksInfo(modifier: Modifier, label: String, value: String) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
    completed: Int,
    inProgress: Int,
    inPlan: Int,
    overdue: Int,
    all: Int,
    selectFilter: (filter: String) -> Unit,
    selectedFilter: String,
) {

    val selectedColor = MaterialTheme.colors.primary
    val unSelectedColor = MaterialTheme.colors.surface
    Box(contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 4.dp)
                ) {
                    val type = TaskStatus.INPLAN_TYPE
                    Column(
                        modifier = Modifier
                            .clickable { selectFilter(type) }
                            .background(
                                color = if (selectedFilter == type) selectedColor else unSelectedColor
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "В плане", fontSize = 25.sp) // В плане
                        Text(text = inPlan.toString(), fontSize = 20.sp)
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                ) {
                    val type = TaskStatus.INPROGRESS_TYPE
                    Column(
                        modifier = Modifier
                            .clickable { selectFilter(type) }
                            .background(
                                color = if (selectedFilter == type) selectedColor else unSelectedColor
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "В процессе", fontSize = 25.sp) // В процессе
                        Text(text = inProgress.toString(), fontSize = 20.sp)
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 4.dp)
                ) {
                    val type = TaskStatus.COMPLITED_TYPE
                    Column(
                        modifier = Modifier
                            .clickable { selectFilter(type) }
                            .background(
                                color = if (selectedFilter == type) selectedColor else unSelectedColor
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Выполнено", fontSize = 25.sp) // Выполнено
                        Text(text = completed.toString(), fontSize = 20.sp)
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                ) {
                    val type = TaskStatus.OVERDUE_TYPE
                    Column(
                        modifier = Modifier
                            .clickable { selectFilter(type) }
                            .background(
                                color = if (selectedFilter == type) selectedColor else unSelectedColor
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Долги", fontSize = 25.sp)// Долги
                        Text(text = overdue.toString(), fontSize = 20.sp)
                    }
                }

            }
        }
        Card(modifier = Modifier.size(45.dp), elevation = 2.dp) {
            val type = TaskStatus.ALL_TASKS
            Box(
                modifier = Modifier
                    .clickable {selectFilter(type)}
                    .background(
                        color = if (selectedFilter == type) selectedColor else unSelectedColor
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = all.toString(), fontSize = 20.sp)
            }
        }
    }
}