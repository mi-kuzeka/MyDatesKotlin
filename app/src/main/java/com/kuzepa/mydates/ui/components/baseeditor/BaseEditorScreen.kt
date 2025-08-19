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
import com.kuzepa.mydates.domain.model.AlertDialogContent
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
    deleteDialogContent: AlertDialogContent,
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
                    onDelete = onDelete,
                    deleteDialogContent = deleteDialogContent
                )
            }
        }
    }
}

@Composable
fun GoBackConfirmationDialog(
    onDismissDialog: () -> Unit,
    onNavigateBack: () -> Unit
) {
    MyDatesAlertDialog(
        onDismissRequest = onDismissDialog,
        onConfirmation = onNavigateBack,
        dialogContent = AlertDialogContent(
            title = stringResource(R.string.go_back_confirmation),
            message = stringResource(R.string.go_back_confirmation_description),
            positiveButtonText = stringResource(R.string.button_confirm_go_back),
            negativeButtonText = stringResource(R.string.keep_editing),
        )
    )
}

@Composable
fun DeleteConfirmationDialog(
    onDismissDialog: () -> Unit,
    onDelete: () -> Unit,
    deleteDialogContent: AlertDialogContent,
) {
    MyDatesAlertDialog(
        onDismissRequest = onDismissDialog,
        onConfirmation = onDelete,
        dialogContent = deleteDialogContent
    )
}