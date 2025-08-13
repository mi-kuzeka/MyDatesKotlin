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
import com.kuzepa.mydates.ui.components.dialog.MyDatesAlertDialog
import com.kuzepa.mydates.ui.components.TopAppBar
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.components.icon.IconDelete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseEditorScreen(
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
    otherDialogs: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler(enabled = !showGoBackConfirmationDialog && !showDeleteDialog) {
        if (hasChanges) {
            onShowGoBackConfirmationDialogChange(true)
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
                        onShowGoBackConfirmationDialogChange(true)
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

            otherDialogs()
        }
    }
}