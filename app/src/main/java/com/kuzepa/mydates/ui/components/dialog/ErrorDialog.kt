package com.kuzepa.mydates.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Error",
    confirmButtonText: String = "OK"
) {
    AlertDialog(
        title = {
            Text(
                text = title,
                color = MyDatesColors.errorTextColor
            )
        },
        text = {
            Text(text = errorMessage)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(confirmButtonText)
            }
        },
        shape = Shapes.defaultDialogShape,
        containerColor = MyDatesColors.screenBackground,
        modifier = modifier,
    )
}


@Preview
@Composable
fun ErrorDialogPreview() {
    MyDatesTheme {
        ErrorDialog(
            errorMessage = "Tadadada",
            onDismissRequest = {}
        )
    }
}