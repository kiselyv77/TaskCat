package com.example.tasksapp.presentation.commonComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    singleLine:Boolean = true,
    value: String,
    label: String,
    isError: Boolean,
    maxChar:Int = 25,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable ()-> Unit,
    onValueChange:(String)->Unit,
    onAction:()->Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text
    )

) {
    OutlinedTextField(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        value = value,
        singleLine = singleLine,
        label = { Text(label) },
        isError = isError,
        trailingIcon = { trailingIcon() },
        onValueChange = {
            if (it.length <= maxChar) {
                onValueChange(it)
            }
        },
        visualTransformation = visualTransformation,
        keyboardActions = KeyboardActions({onAction()},{onAction()},{onAction()},{onAction()},{onAction()},{onAction()}),
        keyboardOptions = keyboardOptions
    )
}