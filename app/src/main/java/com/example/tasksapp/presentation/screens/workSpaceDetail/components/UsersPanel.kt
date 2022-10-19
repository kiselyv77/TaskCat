package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.presentation.commonComponents.TextPlaceHolder

@Composable
fun UsersPanel(
    isPlaceholderVisible: Boolean,
    usersCount: Int,
    clickable: () -> Unit
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

            TextPlaceHolder(
                modifier = Modifier.widthIn(max = screenWidth/2)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = "$usersCount сотрудника",
                fontSize = 20.sp,
                isPlaceholderVisible = isPlaceholderVisible,
                textPlaceHolderLength = 12,
                maxLines = 1
            )

            Image(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp),
                imageVector = Icons.Default.Person,
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp),
                imageVector = Icons.Default.Person,
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp),
                imageVector = Icons.Default.Person,
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
        }
    }
}