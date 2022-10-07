package com.example.tasksapp.presentation.commonComponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbarHost(
    snackbarHostState: SnackbarHostState
) {

    SnackbarHost(
        modifier = Modifier.padding(16.dp),
        hostState = snackbarHostState
    ) { data ->
        Snackbar(
            backgroundColor = Color.White,
            contentColor = MaterialTheme.colors.error,
            action = {
                TextButton(onClick = { data.performAction() }) {
                    Text(
                        color = Color.Black,
                        text = "Повторить",
                    )
                }
            }
        ) {
            Text(
                text = "Ошибка загрузки",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.error,
            )
        }
    }
}