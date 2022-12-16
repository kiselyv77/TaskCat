package com.example.tasksapp.presentation.commonComponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState

@Composable
fun CustomMaterialDialog(
    dialogState: MaterialDialogState,
    text: String,
    onPositiveButton: () -> Unit
) {

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Да") {
                onPositiveButton()
            }
            negativeButton("Отмена")
        }
    ) {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            fontSize = 20.sp,
            text = text
        )
    }
}