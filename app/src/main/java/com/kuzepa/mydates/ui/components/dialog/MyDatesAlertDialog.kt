package com.kuzepa.mydates.ui.components.dialog

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.AlertDialogContent
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
fun MyDatesAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
    dialogContent: AlertDialogContent,
) {
    AlertDialog(
        icon = {
            dialogContent.icon?.let {
                Icon(
                    painter = dialogContent.icon,
                    contentDescription = dialogContent.iconDescription,
                    modifier = Modifier.size(dimensionResource(R.dimen.default_icon_size))
                )
            }
        },
        title = {
            Text(text = dialogContent.title)
        },
        text = {
            if (dialogContent.message.isNotEmpty())
                Text(text = dialogContent.message)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(dialogContent.positiveButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(dialogContent.negativeButtonText)
            }
        },
        shape = Shapes.defaultDialogShape,
        containerColor = MyDatesColors.screenBackground,
        modifier = modifier
    )
}

@Preview
@Composable
fun MyDatesAlertDialogPreview() {
    MyDatesTheme {
        MyDatesAlertDialog(
            onDismissRequest = {},
            onConfirmation = {},
            dialogContent = AlertDialogContent(
                title = "Delete Event?",
                message = "This will permanently delete \"[Event Name]\" from your list.",
                positiveButtonText = "Delete",
                negativeButtonText = "Cancel",
                icon = painterResource(R.drawable.ic_delete)
            )
        )
    }
}