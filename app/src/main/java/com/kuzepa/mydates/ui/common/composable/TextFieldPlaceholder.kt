package com.kuzepa.mydates.ui.common.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.common.composable.color.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
internal fun TextFieldPlaceholder(
    placeholder: String
) {
    Text(
        text = placeholder,
        color = MyDatesColors.placeholderColor,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Preview
@Composable
fun TextFieldPlaceholderPreview() {
    MyDatesTheme {
        TextFieldPlaceholder("Sample")
    }
}