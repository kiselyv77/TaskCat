package com.example.tasksapp.presentation.screens.usersList.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.data.remote.Spec
import com.example.tasksapp.data.remote.Spec.PROTOCOL
import com.example.tasksapp.presentation.commonComponents.AvatarImage
import com.example.tasksapp.util.UserStatus


@Composable
fun UserItem(
    name: String,
    login: String,
    status: String,
    clickable: () -> Unit,
    userStatusToWorkSpace: String
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(3))
            .clickable { clickable() }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AvatarImage(
                imageUrl = "$PROTOCOL${Spec.BASE_URL}/getAvatar/${login}", modifier = Modifier
                    .padding(end = 16.dp)
                    .size(60.dp)
            )
            Column() {
                Text(
                    text = name,
                    fontSize = 20.sp
                )
                Text(
                    text = userStatusToWorkSpace,
                    fontSize = 15.sp
                )
                Text(
                    text = UserStatus.getUserStatusName(status),
                    fontSize = 15.sp,
                    color = UserStatus.getUserStatusColor(status)
                )
            }
        }
    }
}