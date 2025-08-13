package com.kuzepa.mydates.ui.components.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.components.icon.IconClear
import com.kuzepa.mydates.ui.components.supportingtext.MyDatesErrorText
import com.kuzepa.mydates.ui.components.supportingtext.MyDatesSupportingText
import com.kuzepa.mydates.ui.components.supportingtext.MyDatesSupportingTextBox
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
internal fun MyDatesTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    showIconClear: Boolean = true,
    placeholder: String? = null,
    maxLength: Int? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    focusRequester: FocusRequester? = null
) {
    Column(
        modifier = modifier
    ) {
        val textFieldModifier = if (focusRequester == null) {
            Modifier.fillMaxWidth()
        } else {
            Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        }
        TextField(
            value = value,
            onValueChange = {
                val newValue = if (maxLength == null) it else it.take(maxLength)
                onValueChange(newValue)
            },
            colors = MyDatesColors.textFieldColors,
            label = {
                TextFieldLabel(label)
            },
            placeholder = {
                if (placeholder !== null) TextFieldPlaceholder(placeholder)
            },
            trailingIcon = {
                if (showIconClear && value.isNotEmpty()) IconClear(onValueChange)
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            isError = errorMessage != null,
            modifier = textFieldModifier
        )
        MyDatesSupportingTextBox {
            if (errorMessage != null) {
                MyDatesErrorText(errorMessage)
            } else if (maxLength != null) {
                MyDatesSupportingText(
                    text = "${value.length} / $maxLength",
                    color = MyDatesColors.textFieldLabelColor,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
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