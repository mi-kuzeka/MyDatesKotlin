package com.kuzepa.mydates.ui.components.textfield

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun TextFieldLabel(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
    )
}

@Preview
@Composable
fun TextFieldLabelPreview() {
    MyDatesTheme {
        TextFieldLabel("Sample")
    }
}