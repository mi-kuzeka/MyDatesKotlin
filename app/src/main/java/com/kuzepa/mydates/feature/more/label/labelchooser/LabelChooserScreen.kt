package com.kuzepa.mydates.feature.more.label.labelchooser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.usecase.baseeditor.ObjectSaving
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorContentBox
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorDialog
import com.kuzepa.mydates.ui.components.button.MyDatesButton
import com.kuzepa.mydates.ui.components.dropdown.LabelDropDown
import com.kuzepa.mydates.ui.navigation.NavigationResult
import com.kuzepa.mydates.ui.navigation.NavigationResultData
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelChooserScreen(
    viewModel: LabelChooserViewModel = hiltViewModel(),
    eventLabelIdsJson: String,
    onNavigateToLabelEditor: (id: String?) -> Unit,
    onNavigateBack: (result: Int, id: String?) -> Unit,
    labelNavigationResult: NavigationResultData,
    removeNavigationResult: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var showGoBackConfirmationDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.savingFlow.collect { event ->
            when (event) {
                is ObjectSaving.Success -> onNavigateBack(NavigationResult.OK, event.id)
                is ObjectSaving.Error -> {
                    //TODO handle error
                }
            }
        }
    }

    BaseEditorDialog(
        title = stringResource(R.string.choose_label_title),
        isNewItem = true,
        hasChanges = false,
        onNavigateBack = { onNavigateBack(NavigationResult.CANCEL, null) },
        onSave = { viewModel.onEvent(LabelChooserScreenEvent.Save) },
        onDelete = { },
        showDeleteDialog = false,
        showGoBackConfirmationDialog = showGoBackConfirmationDialog,
        onShowDeleteDialogChange = { },
        onShowGoBackDialogChange = { showGoBackConfirmationDialog = it },
        scrollBehavior = scrollBehavior,
        deleteDialogContent = null,
        showDeleteButton = false,
        confirmationButtonText = stringResource(R.string.button_ok)
    ) {
        LabelChooserScreenContent(
            onEvent = { viewModel.onEvent(it) },
            state = state,
            onNavigateToLabelEditor = onNavigateToLabelEditor
        )
    }

    LaunchedEffect(labelNavigationResult.result) {
        if (labelNavigationResult.result == NavigationResult.OK) {
            viewModel.onEvent(LabelChooserScreenEvent.OnLabelNavigationResult(labelNavigationResult))
            removeNavigationResult()
        }
    }
}

@Composable
fun LabelChooserScreenContent(
    onEvent: (LabelChooserScreenEvent) -> Unit,
    state: LabelChooserUiState,
    onNavigateToLabelEditor: (id: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    BaseEditorContentBox(
        modifier = modifier
    ) {
        with(state) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = dimensionResource(R.dimen.padding_small)
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                LabelDropDown(
                    label = selectedLabel,
                    onValueChange = { label ->
                        onEvent(LabelChooserScreenEvent.LabelSelected(label))
                    },
                    options = labels,
                    modifier = Modifier
                        .weight(1f)
                )
                MyDatesButton(
                    icon = Icons.Outlined.Edit,
                    onClick = {
                        selectedLabel?.let {
                            onNavigateToLabelEditor(state.selectedLabel?.id)
                        }
                    }
                )
            }
            MyDatesButton(
                icon = Icons.Outlined.Add,
                text = stringResource(R.string.button_add_label),
                onClick = {
                    onNavigateToLabelEditor(null)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun LabelChooserScreenContentPreview() {
    MyDatesTheme {
        LabelChooserScreenContent(
            onEvent = {},
            state = LabelChooserUiState(),
            onNavigateToLabelEditor = {}
        )
    }
}