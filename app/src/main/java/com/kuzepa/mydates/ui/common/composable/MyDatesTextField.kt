package com.kuzepa.mydates.ui.common.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun MyDatesTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    showIconClear: Boolean,
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    TextField(
        value = value,
        onValueChange = {
            if (maxLength == null || it.length <= maxLength) {
                onValueChange(it)
            }
        },
        label = { FieldLabel(label) },
        trailingIcon = { if (showIconClear) IconClear(onValueChange) },
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = keyboardOptions,
        maxLines = 1,
        supportingText = {
            if (maxLength != null) {
                Text(
                    text = "${value.length} / $maxLength",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun MyDatesTextFieldPreview() {
    MyDatesTheme {
        var text by remember { mutableStateOf("Name") }
        MyDatesTextField(
            label = "Label",
            value = text,
            onValueChange = { text = it },
            showIconClear = true,
            maxLength = 40
        )
    }
}