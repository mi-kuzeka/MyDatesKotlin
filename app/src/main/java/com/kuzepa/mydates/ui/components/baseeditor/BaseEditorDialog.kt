package com.kuzepa.mydates.ui.components.baseeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.components.TopAppBar
import com.kuzepa.mydates.ui.components.dialog.MyDatesAlertDialog
import com.kuzepa.mydates.ui.components.icon.IconDelete
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseEditorDialog(
    title: String,
    isNewItem: Boolean,
    hasChanges: Boolean,
    onNavigateBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    showDeleteDialog: Boolean,
    showGoBackConfirmationDialog: Boolean,
    onShowDeleteDialogChange: (Boolean) -> Unit,
    onShowGoBackConfirmationDialogChange: (Boolean) -> Unit,
    deleteDialogTitle: String,
    deleteDialogText: String,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable () -> Unit
) {
    val onDismissRequest = {
        if (hasChanges) {
            onShowGoBackConfirmationDialogChange(true)
        } else {
            onNavigateBack()
        }
    }
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .wrapContentSize(),
            shape = Shapes.defaultDialogShape,
        ) {
            Column {
                TopAppBar(
                    title = title,
                    canGoBack = false,
                    onGoBack = {},
                    endIcon = {
                        if (!isNewItem) {
                            IconDelete(
                                onClick = { onShowDeleteDialogChange(true) },
                                contentDescription = stringResource(R.string.button_delete)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.fillMaxWidth()
                )
                Surface(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(MyDatesColors.screenBackground)
                ) {
                    content()
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(R.dimen.padding_default),
                            vertical = dimensionResource(R.dimen.padding_small)
                        ),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = dimensionResource(R.dimen.padding_default),
                        alignment = Alignment.End
                    ),
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                    ) {
                        Text(text = stringResource(R.string.button_cancel))
                    }
                    TextButton(
                        onClick = { onSave() },
                    ) {
                        Text(text = stringResource(R.string.button_save))
                    }
                }

                if (showDeleteDialog) {
                    MyDatesAlertDialog(
                        dialogIconImageVector = Icons.Default.Delete,
                        iconDescription = "",
                        dialogTitle = deleteDialogTitle,
                        dialogText = deleteDialogText,
                        confirmButtonText = "Yes, delete", // TODO replace with localized string
                        dismissButtonText = stringResource(R.string.button_cancel),
                        onDismissRequest = { onShowDeleteDialogChange(false) },
                        onConfirmation = {
                            onShowDeleteDialogChange(false)
                            onDelete()
                        }
                    )
                }

                if (showGoBackConfirmationDialog) {
                    MyDatesAlertDialog(
                        dialogTitle = stringResource(R.string.go_back_confirmation),
                        dialogText = stringResource(R.string.go_back_confirmation_description),
                        confirmButtonText = stringResource(R.string.button_confirm_go_back),
                        dismissButtonText = stringResource(R.string.keep_editing),
                        onDismissRequest = { onShowGoBackConfirmationDialogChange(false) },
                        onConfirmation = {
                            onShowGoBackConfirmationDialogChange(false)
                            onNavigateBack()
                        }
                    )
                }
            }
        }
    }
}