package com.kuzepa.mydates.ui.common.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun FieldLabel(label: String) {
    Text(
        text = label,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.labelSmall,
    )
}

@Preview
@Composable
fun FieldLabelPreview() {
    MyDatesTheme {
        FieldLabel("Sample")
    }
}