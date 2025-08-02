package com.kuzepa.mydates.ui.activities.home.event.composable

import android.content.res.Configuration
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
    errorMessage: String,
    validatorHasErrors: Boolean,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    DateTextField(
        value = date,
        onValueChange = onValueChange,
        mask = dateMask,
        delimiter = delimiter,
        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
        maskColor = MaterialTheme.colorScheme.secondary,
        modifier = modifier,
        label = { FieldLabel(label = label) },
        errorMessage = errorMessage,
        validatorHasErrors = validatorHasErrors
    )
}

@Composable
@Preview(name = "DateTextField")
private fun DateFieldPreview() {
    val date = remember { mutableStateOf(TextFieldValue("")) }
    MyDatesTheme {
        EventDateFieldView(
            label = "Date",
            date = date.value,
            dateMask = "dd-mm-yyyy",
            delimiter = '-',
            validatorHasErrors = false,
            errorMessage = "Wrong date format",
            onValueChange = { date.value = it }
        )
    }
}

@Composable
@Preview(name = "DateTextField with error")
private fun DateFieldPreviewError() {
    val date = remember { mutableStateOf(TextFieldValue("2528")) }
    MyDatesTheme {
        EventDateFieldView(
            label = "Date",
            date = date.value,
            dateMask = "dd-mm-yyyy",
            delimiter = '-',
            validatorHasErrors = true,
            errorMessage = "Wrong date format",
            onValueChange = { date.value = it }
        )
    }
}

@Composable
@Preview(
    name = "DateTextField dark", showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
private fun DateFieldPreviewDark() {
    val date = remember { mutableStateOf(TextFieldValue("")) }
    MyDatesTheme {
        EventDateFieldView(
            label = "Date",
            date = date.value,
            dateMask = "dd-mm-yyyy",
            delimiter = '-',
            validatorHasErrors = false,
            errorMessage = "Wrong date format",
            onValueChange = { date.value = it }
        )
    }
}

@Composable
@Preview(
    name = "DateTextField with error dark", showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
private fun DateFieldPreviewErrorDark() {
    val date = remember { mutableStateOf(TextFieldValue("2528")) }
    MyDatesTheme {
        EventDateFieldView(
            label = "Date",
            date = date.value,
            dateMask = "dd-mm-yyyy",
            delimiter = '-',
            validatorHasErrors = true,
            errorMessage = "Wrong date format",
            onValueChange = { date.value = it }
        )
    }
}