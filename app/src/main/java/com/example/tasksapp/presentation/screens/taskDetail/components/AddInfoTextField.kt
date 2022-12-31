package com.example.tasksapp.presentation.screens.taskDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddInfoTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (value: String) -> Unit,
    onClear: () -> Unit,
    onSend: () -> Unit,
    onPickFile: () -> Unit,
    maxChar: Int = 50,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text
    ),
    isFileAttach: Boolean
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        elevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.background(color = MaterialTheme.colors.surface),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .clickable { onPickFile() },
                imageVector = Icons.Default.AttachFile,
                contentDescription = "AttachFile",
            )
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 50.dp, max = 150.dp),
                value = value,
                onValueChange = {
                    if (it.length <= maxChar) {
                        onValueChange(it)
                    }
                },
                label = { Text(text = label) },
                isError = isError,
                keyboardOptions = keyboardOptions,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                )
            )
            if (value.isNotEmpty() || isFileAttach) {
                if(!isFileAttach){
                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .clickable { onClear() },
                        imageVector = Icons.Default.Clear,
                        contentDescription = "clear text",
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .clickable { onSend() },
                    imageVector = Icons.Default.Send,
                    contentDescription = "send",
                )
            }
        }
    }
}