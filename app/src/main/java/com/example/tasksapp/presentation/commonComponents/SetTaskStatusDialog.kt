package com.example.tasksapp.presentation.commonComponents

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tasksapp.util.TaskStatus.TASK_TYPES
import com.example.tasksapp.util.TaskStatus.getTaskStatusName


@Composable
fun SetTaskStatusDialog(
    state: SetTaskStatusDialogState,
    dismiss: () -> Unit,
    setTaskStatus: () -> Unit,
    radioButtonClick:(newStatus:String) -> Unit,
) {
    if(state.isSuccess){
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

            TASK_TYPES.forEach {
                val name = getTaskStatusName(it)
                Row(
                    Modifier.fillMaxWidth().clickable { radioButtonClick(it) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.selectedStatus == it,
                        onClick = { radioButtonClick(it) }
                    )
                    Text(name)
                }
            }
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
                        dismiss()
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
                        setTaskStatus()
                    }) {
                    Text(text = "Изменить")
                }
            }
        }
    }
}

data class SetTaskStatusDialogState(
    val taskId:String = "",
    val selectedStatus:String = "",
    val isOpen:Boolean = false,
    val isSuccess:Boolean = false,
    val isLoading:Boolean = false,
    val error:String = ""
)