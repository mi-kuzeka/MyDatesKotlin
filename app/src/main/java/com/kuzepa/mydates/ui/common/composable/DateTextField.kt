package com.kuzepa.mydates.ui.common.composable

import DateMaskVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import kotlin.math.min

@Composable
fun DateTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    mask: String,
    delimiter: Char,
    textColor: Color,
    maskColor: Color,
    label: @Composable (() -> Unit),
    errorMessage: String,
    validatorHasErrors: Boolean,
    modifier: Modifier = Modifier
) {
    val groups = remember { mask.split(delimiter) }
    val totalDigits = remember { groups.sumOf { it.length } }

    TextField(
        value = value,
        onValueChange = { newValue ->
            // Filter non-digit characters and enforce max length
            val filteredText = newValue.text.filter { it.isDigit() }.take(totalDigits)

            // Calculate new cursor position
            val digitsBeforeCursor = newValue.text
                .substring(0, newValue.selection.start)
                .count { it.isDigit() }
            val newCursor = min(digitsBeforeCursor, filteredText.length)

            onValueChange(
                TextFieldValue(
                    text = filteredText,
                    selection = TextRange(newCursor)
                )
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = DateMaskVisualTransformation(
            mask = mask,
            delimiter = delimiter,
            textColor = textColor,
            maskColor = maskColor
        ),
        isError = validatorHasErrors,
        supportingText = {
            if (validatorHasErrors) {
                Text(text = errorMessage)
            }
        },
        modifier = modifier,
        trailingIcon = {
            if (!value.text.isEmpty()) {
                IconClearWithTextFieldValue(onValueChange = onValueChange)
            }
        },
        label = label
    )
}