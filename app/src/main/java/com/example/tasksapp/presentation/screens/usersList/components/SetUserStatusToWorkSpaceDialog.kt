package com.example.tasksapp.presentation.screens.usersList.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tasksapp.presentation.screens.usersList.SetUserStatusToWorkSpaceDialogState
import com.example.tasksapp.util.UserTypes


@Composable
fun SetUserStatusToWorkSpaceDialog(
    state: SetUserStatusToWorkSpaceDialogState,
    dismiss: () -> Unit,
    setStatus: () -> Unit
) {
    if (state.isSuccess) {
        dismiss()
    }
    if(state.error.isNotEmpty()){
        val context = LocalContext.current
        LaunchedEffect(state.error){
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
        }

    }
    Dialog(
        properties = DialogProperties(),
        onDismissRequest = {
            dismiss()
        },
    ) {
        Column(
            Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = Color.White)
        ) {
            if(state.currentStatus == UserTypes.MEMBER_TYPE){
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = { setStatus() }
                ){
                    Text(text = "Сделать администратором")
                }
            }
            else{
                OutlinedButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = { setStatus() }
                ) {
                    Text(text = "Убрать из администраторов")
                }
            }
        }
    }
}