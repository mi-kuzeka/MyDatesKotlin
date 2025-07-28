package com.kuzepa.mydates.ui.activities.home.event.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.common.composable.DateTextField
import com.kuzepa.mydates.ui.common.composable.FieldLabel
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun EventDateFieldView(
    label: String,
    date: TextFieldValue,
    dateMask: String,
    delimiter: Char,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    DateTextField(
        value = date,
        onValueChange = onValueChange,
        mask = dateMask,
        delimiter = delimiter,
        textColor = MaterialTheme.colorScheme.onPrimary,
        maskColor = MaterialTheme.colorScheme.secondary,
        modifier = modifier,
        label = { FieldLabel(label = label) }
    )
}

@Composable
@Preview
private fun DateFieldPreview() {
    val date = remember { mutableStateOf(TextFieldValue("")) }
    MyDatesTheme {
        EventDateFieldView(
            label = "Date",
            date = date.value,
            dateMask = "dd-mm-yyyy",
            delimiter = '-',
            onValueChange = { date.value = it }
        )
    }
}