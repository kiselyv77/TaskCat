package com.example.tasksapp.presentation.screens.usersList.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.util.UserStatus
import com.example.tasksapp.util.UserStatus.getUserStatusName


@Composable
fun UserItem(
    name: String,
    login: String,
    status:String,
    clickable: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp).clip(RoundedCornerShape(3))
            .clickable { clickable() }
    ) {
        Column(modifier = Modifier.padding(16.dp),) {
            Text(
                text = name,
                fontSize = 30.sp
            )
            Text(
                text = login,
                fontSize = 20.sp
            )
            Text(
                text = getUserStatusName(status),
                fontSize = 15.sp,
                color = if(status == UserStatus.ONLINE_STATUS) Color.Green else MaterialTheme.colors.onBackground
            )
        }
    }

}