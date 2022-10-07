package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.tasksapp.presentation.commonComponents.TextPlaceHolder


@Composable
fun ItemTask(
    modifier: Modifier,
    count: Int,
    name: String,
    description: String,
    isPlaceholdersVisible: Boolean
) {
    Card(
        modifier = modifier,
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Text(
                    text = "$count)",
                    fontSize = 30.sp
                )
                TextPlaceHolder(
                    modifier = Modifier,
                    text = name,
                    fontSize = 25.sp,
                    isPlaceholderVisible = isPlaceholdersVisible
                )
            }

            TextPlaceHolder(
                modifier = Modifier
                    .padding(),
                text = description,
                fontSize = 20.sp,
                isPlaceholderVisible = isPlaceholdersVisible,
                textAlign = TextAlign.Center
            )
        }
    }
}