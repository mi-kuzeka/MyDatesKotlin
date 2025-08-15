package com.kuzepa.mydates.ui.components.baseeditor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.components.TopAppBar
import com.kuzepa.mydates.ui.components.dialog.MyDatesAlertDialog
import com.kuzepa.mydates.ui.components.icon.IconDelete
import com.kuzepa.mydates.ui.theme.MyDatesColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseEditorScreen(
    title: String,
    isNewItem: Boolean,
    hasChanges: Boolean,
    onNavigateBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    showGoBackDialog: Boolean,
    showDeleteDialog: Boolean,
    onShowGoBackDialogChange: (Boolean) -> Unit,
    onShowDeleteDialogChange: (Boolean) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable () -> Unit
) {
    val handleGoBack = !showGoBackDialog && !showDeleteDialog
    BackHandler(enabled = handleGoBack) {
        if (hasChanges) {
            onShowGoBackDialogChange(true)
        } else {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = title,
                canGoBack = true,
                onGoBack = {
                    if (hasChanges) {
                        onShowGoBackDialogChange(true)
                    } else {
                        onNavigateBack()
                    }
                },
                scrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxWidth(),
                endIcon = {
                    if (!isNewItem) {
                        IconDelete(
                            onClick = { onShowDeleteDialogChange(true) },
                            contentDescription = stringResource(R.string.button_delete)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSave,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.button_save)
                )
            }
        },
        // Connects the Scaffold's scroll to the TopAppBar's collapse/expand behavior
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Vertical,
                    ),
                )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MyDatesColors.screenBackground)
            ) {
                content()
            }

            if (showGoBackDialog) {
                GoBackConfirmationDialog(
                    onDismissDialog = { onShowGoBackDialogChange(false) },
                    onNavigateBack = onNavigateBack
                )
            }
            if (showDeleteDialog) {
                DeleteConfirmationDialog(
                    onDismissDialog = { onShowDeleteDialogChange(false) },
                    onDelete = onDelete
                )
            }
        }
    }
}

@Composable
private fun GoBackConfirmationDialog(
    onDismissDialog: () -> Unit,
    onNavigateBack: () -> Unit
) {
    MyDatesAlertDialog(
        dialogTitle = stringResource(R.string.go_back_confirmation),
        dialogText = stringResource(R.string.go_back_confirmation_description),
        confirmButtonText = stringResource(R.string.button_confirm_go_back),
        dismissButtonText = stringResource(R.string.keep_editing),
        onDismissRequest = onDismissDialog,
        onConfirmation = onNavigateBack
    )
}

@Composable
private fun DeleteConfirmationDialog(
    onDismissDialog: () -> Unit,
    onDelete: () -> Unit
) {
    MyDatesAlertDialog(
        dialogIconImageVector = Icons.Default.Delete,
        iconDescription = "",
        dialogTitle = "dialogTitle", // TODO replace with localized string
        dialogText = "dialogText", // TODO replace with localized string
        confirmButtonText = "Yes, delete", // TODO replace with localized string
        dismissButtonText = stringResource(R.string.button_cancel),
        onDismissRequest = onDismissDialog,
        onConfirmation = onDelete
    )
}