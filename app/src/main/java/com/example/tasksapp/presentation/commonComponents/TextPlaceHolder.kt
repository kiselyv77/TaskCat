package com.example.tasksapp.presentation.commonComponents

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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
    isPlaceholderVisible: Boolean,
    textAlign: TextAlign? = null,
    textPlaceHolderLength: Int = 15
) {
    val valueText = if(isPlaceholderVisible) "A".repeat(textPlaceHolderLength) else text
    val color = if(isPlaceholderVisible) Color.White.copy(alpha = 0.0f) else Color.Unspecified
    Text(
        modifier = modifier.placeholder(
            visible = isPlaceholderVisible,
            shape = RoundedCornerShape(4.dp),
            highlight = PlaceholderHighlight.shimmer()
        ),
        text = valueText,
        fontSize = fontSize,
        textAlign = textAlign,
        color = color
    )
}