package com.kuzepa.mydates.ui.common.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.ui.common.composable.shape.MyDatesShapes
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun MyDatesAlertDialog(
    dialogTitle: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
    dialogText: String? = null,
    dialogIconImageVector: ImageVector? = null,
    iconDescription: String = "",
) {
    AlertDialog(
        icon = {
            dialogIconImageVector?.let {
                Icon(
                    imageVector = dialogIconImageVector,
                    contentDescription = iconDescription
                )
            }
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            dialogText?.let {
                Text(text = dialogText)
            }
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
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(dismissButtonText)
            }
        },
        shape = MyDatesShapes.defaultDialogShape,
        modifier = modifier
    )
}

@Preview
@Composable
fun MyDatesAlertDialogPreview() {
    MyDatesTheme {
        MyDatesAlertDialog(
            dialogIconImageVector = Icons.Default.Delete,
            iconDescription = "",
            dialogTitle = "Delete this event?",
            dialogText = "You can't restore it after deleting",
            confirmButtonText = "Yes, delete",
            dismissButtonText = "Cancel",
            onDismissRequest = {},
            onConfirmation = {}
        )
    }
}