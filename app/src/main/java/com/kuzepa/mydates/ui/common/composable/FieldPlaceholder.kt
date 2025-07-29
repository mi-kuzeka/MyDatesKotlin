package com.kuzepa.mydates.ui.common.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun FieldPlaceholder(
    placeholder: String
) {
    Text(
        text = placeholder,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Preview
@Composable
fun FieldPlaceholderPreview() {
    MyDatesTheme {
        FieldPlaceholder("Sample")
    }
}