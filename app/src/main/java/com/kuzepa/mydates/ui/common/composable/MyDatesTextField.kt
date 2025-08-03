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
internal fun MyDatesTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    showIconClear: Boolean = true,
    placeholder: String? = null,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    TextField(
        value = value,
        onValueChange = {
            val newValue = if (maxLength == null) it else it.take(maxLength)
            onValueChange(newValue)
        },
        colors = getMyDatesTextFieldColors(),
        label = {
            FieldLabel(label)
        },
        placeholder = {
            if (placeholder !== null) FieldPlaceholder(placeholder)
        },
        trailingIcon = {
            if (showIconClear && !value.isEmpty()) IconClear(onValueChange)
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        isError = errorMessage != null,
        supportingText = {
            if (errorMessage != null) {
                Text(text = errorMessage)
            } else if (maxLength != null) {
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

@Preview(name = "TextField")
@Composable
fun MyDatesTextFieldPreview() {
    MyDatesTheme {
        var text by remember { mutableStateOf("Steve") }
        MyDatesTextField(
            label = "Name",
            value = text,
            onValueChange = { text = it },
            showIconClear = true,
            maxLength = 10,
            errorMessage = null
        )
    }
}

@Preview(name = "TextField with error")
@Composable
fun MyDatesTextFieldPreviewError() {
    MyDatesTheme {
        var text by remember { mutableStateOf("") }
        MyDatesTextField(
            label = "Name",
            value = text,
            onValueChange = { text = it },
            showIconClear = true,
            maxLength = 10,
            errorMessage = "This field is required"
        )
    }
}