package com.example.tasksapp.presentation.commonComponents

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun CustomAlertDialog(
    label: String,
    dismiss: () -> Unit,
    ok: () -> Unit,
    state: CustomAlertDialogState,
    onSuccess:() -> Unit,
) {

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
            Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = Color.White)
        ) {
            Text(modifier = Modifier.padding(16.dp), text = label,  fontSize = 20.sp,)
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                }
                if (state.error.isNotEmpty()) {
                    val context = LocalContext.current
                    LaunchedEffect(state.error) {
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
                        ok()
                    }) {
                    Text(text = "Ок")
                }
            }
        }
    }
}

data class CustomAlertDialogState(
    val value: String = "",
    val isOpen: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = "",
    val isSuccess: Boolean = false
)
