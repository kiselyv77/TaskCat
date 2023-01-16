package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.data.remote.Spec
import com.example.tasksapp.data.remote.Spec.PROTOCOL
import com.example.tasksapp.presentation.commonComponents.AvatarImage
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
                text = "$usersCount сотрудников",
                fontSize = 20.sp,
                isPlaceholderVisible = isPlaceholderVisible,
                textPlaceHolderLength = 12,
                maxLines = 1
            )
            firstUsers.forEach { user->
                user?.let{
                    AvatarImage(
                        imageUrl = "$PROTOCOL${Spec.BASE_URL}/getAvatar/${it}",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(avatarsSize)

                    )
                }
            }
        }
    }
}