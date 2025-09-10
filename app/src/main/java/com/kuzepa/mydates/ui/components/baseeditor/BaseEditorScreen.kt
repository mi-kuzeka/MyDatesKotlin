package com.kuzepa.mydates.ui.components.baseeditor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.AlertDialogContent
import com.kuzepa.mydates.domain.model.TextFieldMaxLength
import com.kuzepa.mydates.feature.home.event.components.EventDateField
import com.kuzepa.mydates.feature.home.event.components.EventImageChooser
import com.kuzepa.mydates.feature.home.event.components.EventLabelContainer
import com.kuzepa.mydates.ui.components.MyDatesCheckbox
import com.kuzepa.mydates.ui.components.MyDatesExposedDropDown
import com.kuzepa.mydates.ui.components.TopAppBar
import com.kuzepa.mydates.ui.components.dialog.MyDatesAlertDialog
import com.kuzepa.mydates.ui.components.icon.IconDelete
import com.kuzepa.mydates.ui.components.textfield.MyDatesTextField
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MyDatesColors.screenBackground)
                    .align(alignment = Alignment.TopCenter)
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BaseEditorScreenPreview() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    MyDatesTheme {
        BaseEditorScreen(
            title = "Title",
            isNewItem = true,
            hasChanges = false,
            onNavigateBack = {},
            onSave = {},
            onDelete = {},
            showGoBackDialog = false,
            showDeleteDialog = false,
            onShowGoBackDialogChange = {},
            onShowDeleteDialogChange = {},
            deleteDialogContent = AlertDialogContent(
                title = "Delete this event?",
                message = "This action can't be undone",
                positiveButtonText = "Delete",
                negativeButtonText = "Cancel",
                icon = Icons.Default.Delete
            ),
            scrollBehavior = scrollBehavior,
        ) {
            BaseEditorContentBox(
                addSpacerForFabButton = true,
                modifier = Modifier
            ) {
                EventImageChooser(
                    null,
                    chooseImage = {},
                    rotateLeft = {},
                    rotateRight = {},
                    removeImage = {},
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                MyDatesTextField(
                    label = "Name",
                    value = "",
                    onValueChange = { },
                    errorMessage = null,
                    maxLength = TextFieldMaxLength.NAME.length,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    focusRequester = null,
                    modifier = Modifier.fillMaxWidth()
                )
                EventDateField(
                    label = stringResource(R.string.date_label),
                    date = "",
                    dateMask = "mm/dd/yyyy",
                    delimiter = '/',
                    errorMessage = "dateValidationError",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    checkBox = {
                        MyDatesCheckbox(
                            checked = false,
                            onCheckedChange = { },
                            text = "No year",
                            textStyle = MaterialTheme.typography.bodySmall
                        )
                    }
                )
                MyDatesExposedDropDown(
                    label = "Event type",
                    value = "Birthday",
                    onValueChange = { },
                    errorMessage = "",
                    options = listOf(),
                    onAddNewItem = {},
                    addNewItemLabel = "Add new event type",
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Select event type"
                )
                EventLabelContainer(
                    label = "Tags",
                    labels = listOf(),
                    onLabelClick = { },
                    onRemoveLabelClick = { },
                    buttonRemoveDescription = "Remove tags",
                    addLabelText = "Add tag",
                    onAddLabelClick = { },
                    modifier = Modifier.fillMaxWidth()
                )
                MyDatesTextField(
                    label = "Notes",
                    value = "",
                    onValueChange = { },
                    maxLength = TextFieldMaxLength.NOTES.length,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    placeholder = "Additional information",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}