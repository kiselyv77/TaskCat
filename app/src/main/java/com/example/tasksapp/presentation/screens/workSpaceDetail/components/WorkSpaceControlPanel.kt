package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp


@Composable
fun WorkSpaceControlPanel(modifier: Modifier = Modifier, addTask: () -> Unit) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ){
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp),
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                )
            }
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                        .clickable { addTask() },
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                )
            }
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp),
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)

                )
            }
        }
    }
}