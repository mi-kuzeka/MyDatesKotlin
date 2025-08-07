package com.kuzepa.mydates.ui.activities.home.event.composable

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.common.composable.TextFieldLabel
import com.kuzepa.mydates.ui.common.composable.masktextfield.DateTextField
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
internal fun EventDateField(
    label: String,
    date: String,
    dateMask: String,
    delimiter: Char,
    errorMessage: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    checkBox: @Composable () -> Unit = {}
) {
    DateTextField(
        value = date,
        onValueChange = onValueChange,
        mask = dateMask,
        delimiter = delimiter,
        label = { TextFieldLabel(text = label) },
        errorMessage = errorMessage,
        modifier = modifier,
        checkBox = checkBox
    )
}

@Composable
@Preview(name = "DateTextField")
private fun DateFieldPreview() {
    val date = remember { mutableStateOf("") }
    MyDatesTheme {
        EventDateField(
            label = "Date",
            date = date.value,
            dateMask = "dd-mm-yyyy",
            delimiter = '-',
            errorMessage = null,
            onValueChange = { date.value = it }
        )
    }
}

@Composable
@Preview(name = "DateTextField with error")
private fun DateFieldPreviewError() {
    val date = remember { mutableStateOf("2528") }
    MyDatesTheme {
        EventDateField(
            label = "Date",
            date = date.value,
            dateMask = "dd-mm-yyyy",
            delimiter = '-',
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
    val date = remember { mutableStateOf("") }
    MyDatesTheme {
        EventDateField(
            label = "Date",
            date = date.value,
            dateMask = "dd-mm-yyyy",
            delimiter = '-',
            errorMessage = null,
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
    val date = remember { mutableStateOf("2528") }
    MyDatesTheme {
        EventDateField(
            label = "Date",
            date = date.value,
            dateMask = "dd-mm-yyyy",
            delimiter = '-',
            errorMessage = "Wrong date format",
            onValueChange = { date.value = it }
        )
    }
}