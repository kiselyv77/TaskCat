package com.example.tasksapp.presentation.screens.taskDetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.example.tasksapp.data.remote.Spec
import com.example.tasksapp.domain.model.UserModel
import com.example.tasksapp.presentation.commonComponents.AvatarImage

@Composable
fun UsersRow(
    userList:List<UserModel>,
    addUserOpenDialog:()->Unit,
) {
    val avatarsSize = 40.dp

    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End){
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                items(userList) {
                    AvatarImage(
                        imageUrl = "https://${Spec.BASE_URL}/getAvatar/${it.login}",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(avatarsSize)
                    )
                }
            }
            IconButton(
                onClick = { addUserOpenDialog() }
            ) {
                Image(
                    modifier = Modifier
                        .size(55.dp)
                        .padding(8.dp).clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape),
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                )
            }
        }

    }
}