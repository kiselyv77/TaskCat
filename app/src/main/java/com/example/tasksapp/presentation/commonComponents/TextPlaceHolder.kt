package com.example.tasksapp.presentation.commonComponents

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun TextPlaceHolder(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit,
    isPlaceholderVisible:Boolean
) {
    val valueText = if(isPlaceholderVisible) "Placeholder:)" else text
    Text(
        modifier = modifier.placeholder(
            visible = isPlaceholderVisible,
            shape = RoundedCornerShape(4.dp),
            highlight = PlaceholderHighlight.shimmer()
        ),
        text = valueText,
        fontSize = fontSize
    )
}