package com.kuzepa.mydates.ui.common.composable.masktextfield

import MaskVisualTransformation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.kuzepa.mydates.ui.common.composable.color.MyDatesColors
import com.kuzepa.mydates.ui.common.composable.icon.IconClear
import com.kuzepa.mydates.ui.common.composable.supportingtext.MyDatesErrorText
import com.kuzepa.mydates.ui.common.composable.supportingtext.MyDatesSupportingTextBox

@Composable
fun DateTextField(
    label: @Composable (() -> Unit),
    value: String,
    onValueChange: (String) -> Unit,
    mask: String,
    delimiter: Char,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    checkBox: @Composable () -> Unit = {}
) {
    val maxDigits = remember(mask) { mask.split(delimiter).sumOf { it.length } }

    Column(
        modifier = modifier
    ) {
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
                textColor = MyDatesColors.defaultTextColor,
                maskColor = MyDatesColors.placeholderColor
            ),
            isError = errorMessage != null,
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconClear(onValueChange = onValueChange)
                }
            },
            colors = MyDatesColors.textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )
        MyDatesSupportingTextBox {
            Column(modifier = Modifier.fillMaxWidth()) {
                errorMessage?.let {
                    MyDatesErrorText(errorMessage)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    checkBox()
                }
            }
        }
    }
}