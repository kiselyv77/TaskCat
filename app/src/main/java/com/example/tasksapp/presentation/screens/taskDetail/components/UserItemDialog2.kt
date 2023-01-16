package com.example.tasksapp.presentation.screens.taskDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.example.tasksapp.data.remote.Spec.PROTOCOL
import com.example.tasksapp.presentation.commonComponents.AvatarImage
import com.example.tasksapp.presentation.screens.taskDetail.UserItemDialogState2
import com.example.tasksapp.util.UserStatus

@Composable
fun UserItemDialog2(
    state: UserItemDialogState2,
    myLogin: String,
    isCreator: Boolean,
    onSuccess: () -> Unit,
    dismiss: () -> Unit,
    deleteUser: (loginUser: String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    if (state.isSuccess) {
        onSuccess()
    }
    Dialog(
        properties = DialogProperties(),
        onDismissRequest = {
            dismiss()
        },
    ) {
        Column(
            modifier = Modifier.size(height = screenHeight / 3f, width = screenWidth / 1.5f)
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarImage(
                imageUrl = "$PROTOCOL${Spec.BASE_URL}/getAvatar/${state.userModel.login}",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(120.dp)
            )
            Text(
                text = state.userModel.name,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = state.userModel.login,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = UserStatus.getUserStatusName(state.userModel.status),
                color = UserStatus.getUserStatusColor(state.userModel.status),
                textAlign = TextAlign.Center
            )
            if (isCreator && myLogin != state.userModel.login) {
                OutlinedButton(
                    onClick = { deleteUser(state.userModel.login) }
                ) {
                    Text(text = "Удалить пользователя")
                }
            }
        }
    }
}