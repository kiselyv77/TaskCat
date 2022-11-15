package com.example.tasksapp.presentation.screens.messenger.components

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomMessageField(
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean = false,
    maxChar: Int = 25,
    onValueChange: (String) -> Unit,
    send: () -> Unit,
    clear: () -> Unit,
    startVoiceRecord: () -> Unit,
    stopVoiceRecord: () -> Unit,
    isVoiceRecording: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text
    )
) {
    Surface(
        modifier = modifier,
        elevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.background(color = MaterialTheme.colors.surface),
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .clickable { },
                imageVector = Icons.Default.AttachFile,
                contentDescription = "clear text",
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
                label = { Text(text = "Сообщение") },
                isError = isError,
                keyboardOptions = keyboardOptions,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                )
            )
            if (value.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .clickable { clear() },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear text",
                )
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .clickable { send() },
                    imageVector = Icons.Default.Send,
                    contentDescription = "clear text",
                )
            } else {
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .pointerInteropFilter {
                            when (it.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    startVoiceRecord()
                                }
                                MotionEvent.ACTION_UP -> {
                                    stopVoiceRecord()
                                }
                            }
                            true
                        },
                    imageVector = Icons.Default.KeyboardVoice,
                    contentDescription = "clear text",
                )
            }
        }

    }
}
