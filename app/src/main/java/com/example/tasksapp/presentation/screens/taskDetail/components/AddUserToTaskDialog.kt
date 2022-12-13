package com.example.tasksapp.presentation.screens.taskDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tasksapp.data.remote.Spec
import com.example.tasksapp.presentation.commonComponents.AvatarImage
import com.example.tasksapp.presentation.screens.taskDetail.AddUserToTaskDialogState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddUserToTaskDialog(
    onUserSelect: (userLogin: String) -> Unit,
    dismiss: () -> Unit,
    state: AddUserToTaskDialogState,
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            dismiss()
        }) {
        Box(
            Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = Color.White)
                .size(height = screenHeight / 3f, width = screenWidth / 1.5f),
            contentAlignment = Alignment.TopCenter
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    Modifier.align(
                        Alignment.Center
                    )
                )
            }
            if(state.error.isNotEmpty()){
                Text(
                    modifier = Modifier.align(
                        Alignment.Center
                    ).padding(16.dp),
                    text = state.error,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
            if (state.isSuccess && state.displayedList.isEmpty()) {
                Text(
                    modifier = Modifier.align(
                        Alignment.Center
                    ).padding(16.dp),
                    text = "Вы пригласили уже всех пользователей",
                    textAlign = TextAlign.Center
                )
            }

            Column() {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Пригласите пользователей в эту задачу",
                    textAlign = TextAlign.Center
                )
                LazyColumn(Modifier.padding(16.dp)) {
                    items(state.displayedList) { user ->
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp)
                                .clip(RoundedCornerShape(3))
                                .clickable { onUserSelect(user.login) }
                        ) {
                            Row(
                                modifier = Modifier.padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                AvatarImage(
                                    imageUrl = "https://${Spec.BASE_URL}/getAvatar/${user.login}",
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .size(40.dp)
                                )
                                Column() {
                                    Text(
                                        text = user.name,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = user.login,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

