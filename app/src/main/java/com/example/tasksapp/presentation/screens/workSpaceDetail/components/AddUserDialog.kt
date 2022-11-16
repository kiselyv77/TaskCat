package com.example.tasksapp.presentation.screens.workSpaceDetail.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tasksapp.presentation.commonComponents.CustomTextField
import com.example.tasksapp.presentation.screens.workSpaceDetail.AddUserDialogState


@Composable
fun AddUserDialog(
    state: AddUserDialogState,
    dismiss: () -> Unit,
    onLoginUserChanged: (newLoginUser:String) -> Unit,
    addUser: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    if(state.isSuccess){
        onLoginUserChanged("")
        dismiss()
    }
    Dialog(
        properties = DialogProperties(),
        onDismissRequest = {
            dismiss()
        },
    ){
        Column(
            Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = Color.White)
        ) {
            CustomTextField(
                modifier = Modifier,
                value = state.userLogin,
                label = "login user",
                isError = state.error.isNotEmpty(),
                trailingIcon = { /*TODO*/ },
                onValueChange = { onLoginUserChanged(it) },
                onAction = {focusManager.clearFocus()}
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                if(state.isLoading){
                    CircularProgressIndicator()
                }
                if(state.error.isNotEmpty()){
                    val context = LocalContext.current
                    LaunchedEffect(state.error){
                        Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                    }
                }
                OutlinedButton(
                    modifier = Modifier.padding(end = 16.dp, start = 16.dp, bottom = 8.dp),
                    onClick = { dismiss() }) {
                    Text(text = "Отмена")
                }
                OutlinedButton(
                    modifier = Modifier.padding(end = 16.dp, start = 16.dp, bottom = 8.dp),
                    onClick = {
                        addUser()
                    }) {
                    Text(text = "Пригласить")
                }
            }
        }
    }
}