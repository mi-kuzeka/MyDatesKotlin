package com.kuzepa.mydates.ui.components.baseeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.kuzepa.mydates.domain.model.AlertDialogContent
import com.kuzepa.mydates.ui.components.TopAppBar
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
    scrollBehavior: TopAppBarScrollBehavior,
    onDelete: () -> Unit = {},
    showDeleteDialog: Boolean = false,
    showGoBackConfirmationDialog: Boolean = false,
    onShowDeleteDialogChange: (Boolean) -> Unit = {},
    onShowGoBackDialogChange: (Boolean) -> Unit ={},
    deleteDialogContent: AlertDialogContent? = null,
    showDeleteButton: Boolean = true,
    confirmationButtonText: String? = null,
    content: @Composable () -> Unit
) {
    val onDismissRequest = {
        if (hasChanges) {
            onShowGoBackDialogChange(true)
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
            Column(
                modifier = Modifier
                    .wrapContentHeight()
            ) {
                TopAppBar(
                    title = title,
                    canGoBack = false,
                    onGoBack = {},
                    endIcon = {
                        if (showDeleteButton && !isNewItem) {
                            IconDelete(
                                onClick = { onShowDeleteDialogChange(true) },
                                contentDescription = stringResource(R.string.button_delete)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(MyDatesColors.screenBackground)
                        .weight(1f, fill = false)
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
                        Text(
                            text = confirmationButtonText ?: stringResource(R.string.button_save)
                        )
                    }
                }

                if (showDeleteDialog && deleteDialogContent != null) {
                    DeleteConfirmationDialog(
                        onDismissDialog = { onShowDeleteDialogChange(false) },
                        onDelete = onDelete,
                        deleteDialogContent = deleteDialogContent
                    )
                }

                if (showGoBackConfirmationDialog) {
                    GoBackConfirmationDialog(
                        onDismissDialog = { onShowGoBackDialogChange(false) },
                        onNavigateBack = onNavigateBack
                    )
                }
            }
        }
    }
}