package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tasksapp.data.remote.Spec
import com.example.tasksapp.presentation.commonComponents.TextPlaceHolder

@Composable
fun UsersPanel(
    isPlaceholderVisible: Boolean,
    usersCount: Int,
    clickable: () -> Unit,
    firstUsers: List<String?>
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.clickable { clickable() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ){
            val avatarsSize = 40.dp


            TextPlaceHolder(
                modifier = Modifier
                    .widthIn(max = screenWidth / 2)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = "$usersCount сотрудника",
                fontSize = 20.sp,
                isPlaceholderVisible = isPlaceholderVisible,
                textPlaceHolderLength = 12,
                maxLines = 1
            )

            firstUsers.getOrNull(0)?.let{
                AsyncImage(
                    model = "https://${Spec.BASE_URL}/getAvatar/${it}?",
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(avatarsSize)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            }
            firstUsers.getOrNull(1)?.let{
                AsyncImage(
                    model = "https://${Spec.BASE_URL}/getAvatar/${it}?",
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(avatarsSize)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            }
            firstUsers.getOrNull(2)?.let{
                AsyncImage(
                    model = "https://${Spec.BASE_URL}/getAvatar/${it}?",
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(avatarsSize)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            }

        }
    }
}