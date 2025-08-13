package com.kuzepa.mydates.ui.components.supportingtext

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.kuzepa.mydates.ui.theme.MyDatesColors

@Composable
fun MyDatesSupportingText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MyDatesColors.placeholderColor,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        color = color,
        textAlign = textAlign,
        style = style,
        modifier = modifier.fillMaxWidth()
    )
}