package com.example.tasksapp.presentation.screens.addWorkSpace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasksapp.presentation.commonComponents.CloseIconTextField
import com.example.tasksapp.presentation.commonComponents.CustomTextField
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination()
fun AddWorkSpaceScreen(viewModel: AddWorkSpaceViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.fillMaxSize().clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            focusManager.clearFocus()
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomTextField(
            value = state.name,
            label = "name",
            isError = viewModel.state.value.error.isNotEmpty(),
            trailingIcon = { CloseIconTextField { viewModel.setName("") } },
            onValueChange = { viewModel.setName(it) },
            onAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            )
        )

        CustomTextField(
            modifier = Modifier.defaultMinSize(minHeight = 200.dp),
            value = state.description,
            singleLine = false,
            maxChar = 500,
            label = "description",
            isError = viewModel.state.value.error.isNotEmpty(),
            trailingIcon = { CloseIconTextField { viewModel.setDescription("") } },
            onValueChange = { viewModel.setDescription(it) },
            onAction = { focusManager.clearFocus() },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            )
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Добавить")
        }
    }

}









