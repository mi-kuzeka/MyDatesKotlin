package com.kuzepa.mydates.ui.common.composable

import MaskVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DateTextField(
    label: @Composable (() -> Unit),
    value: String,
    onValueChange: (String) -> Unit,
    mask: String,
    delimiter: Char,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    val maxDigits = remember { mask.split(delimiter).sumOf { it.length } }

    TextField(
        label = label,
        value = value,
        onValueChange = { newValue ->
            // Filter non-digit characters and enforce max length
            val filteredText = newValue.filter { it.isDigit() }.take(maxDigits)
            onValueChange(filteredText)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = MaskVisualTransformation(
            mask = mask,
            delimiters = charArrayOf(delimiter),
            textColor = getMyDatesTextFieldColors().focusedTextColor,
            maskColor = getMyDatesPlaceholderColor()
        ),
        isError = errorMessage != null,
        supportingText = {
            if (errorMessage != null) {
                Text(text = errorMessage)
            }
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconClear(onValueChange = onValueChange)
            }
        },
        colors = getMyDatesTextFieldColors(),
        modifier = modifier,
    )
}