package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.Image
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
import java.util.*


@Composable
fun WorkSpaceControlPanel(
    modifier: Modifier = Modifier,
    addTask: () -> Unit,
    addUser: () -> Unit
) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ){
            IconButton(
                onClick = { addUser() }
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
                onClick = { addTask() }
            ) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                )
            }
            IconButton(
                onClick = { }
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

fun getC(){

    val cur = Currency.getInstance(Locale.getDefault()).currencyCode

}