package com.example.tasksapp.presentation.commonComponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbarHost(
    snackbarHostState: SnackbarHostState,
    padding: Dp = 16.dp
) {
    SnackbarHost(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = padding),
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