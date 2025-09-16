package com.kuzepa.mydates.feature.more.label

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.common.util.labelcolor.getContrastedColor
import com.kuzepa.mydates.domain.model.AlertDialogContent
import com.kuzepa.mydates.domain.model.TextFieldMaxLength
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorContentBox
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorDialog
import com.kuzepa.mydates.ui.components.baseeditor.HandleEditorResults
import com.kuzepa.mydates.ui.components.selectioncontainer.ColorSelectionContainer
import com.kuzepa.mydates.ui.components.selectioncontainer.IconSelectionContainer
import com.kuzepa.mydates.ui.components.textfield.MyDatesTextField
import com.kuzepa.mydates.ui.navigation.NavigationResult
import kotlinx.coroutines.android.awaitFrame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelScreen(
    viewModel: LabelViewModel = hiltViewModel(),
    id: String?,
    onNavigateBack: (result: Int, id: String?) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showGoBackConfirmationDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (state.isNewLabel) {
            // Request focus when the dialog appears
            awaitFrame()
            focusRequester.requestFocus()
        }
    }

    HandleEditorResults(
        savingFlow = viewModel.savingFlow,
        deletingFlow = viewModel.deletingFlow,
        onSuccess = { id -> onNavigateBack(NavigationResult.OK, id) },
        onError = { /* TODO show error */ }
    )

    BaseEditorDialog(
        title = stringResource(
            if (state.isNewLabel) R.string.label_creator_title else R.string.label_editor_title
        ),
        isNewItem = state.isNewLabel,
        hasChanges = state.hasChanges,
        onNavigateBack = { onNavigateBack(NavigationResult.CANCEL, null) },
        onSave = { viewModel.onEvent(LabelScreenEvent.Save) },
        onDelete = { viewModel.onEvent(LabelScreenEvent.Delete) },
        showDeleteDialog = showDeleteDialog,
        showGoBackConfirmationDialog = showGoBackConfirmationDialog,
        onShowDeleteDialogChange = { showDeleteDialog = it },
        onShowGoBackDialogChange = { showGoBackConfirmationDialog = it },
        scrollBehavior = scrollBehavior,
        deleteDialogContent = AlertDialogContent(
            title = context.getString(
                R.string.delete_dialog_title_pattern,
                stringResource(R.string.this_label)
            ),
            message = context.getString(
                R.string.delete_dialog_msg_pattern,
                stringResource(R.string.this_label)
            ),
            positiveButtonText = stringResource(R.string.button_delete),
            negativeButtonText = stringResource(R.string.button_cancel),
            icon = Icons.Default.Delete
        ),
    ) {
        LabelScreenContent(
            onEvent = { viewModel.onEvent(it) },
            state = state,
            focusRequester = focusRequester,
        )
    }
}

@Composable
fun LabelScreenContent(
    onEvent: (LabelScreenEvent) -> Unit,
    state: LabelUiState,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    BaseEditorContentBox(
        modifier = modifier
    ) {
        with(state) {
            // TODO Preview
            MyDatesTextField(
                label = stringResource(R.string.name_label),
                value = name,
                onValueChange = { onEvent(LabelScreenEvent.NameChanged(it)) },
                errorMessage = nameValidationError,
                maxLength = TextFieldMaxLength.NAME.length,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                focusRequester = focusRequester,
                modifier = Modifier.fillMaxWidth()
            )
            ColorSelectionContainer(
                containerLabel = stringResource(R.string.label_color),
                selectedColorId = colorId,
                onSelected = { newColor -> onEvent(LabelScreenEvent.ColorChanged(newColor)) },
                onSelectCustomColor = { customColor ->
                    // TODO show color picker
                },
                modifier = Modifier.fillMaxWidth()
            )
            val color = remember(colorId) { LabelColor.getColorFromId(colorId) }
            val iconColor = remember(colorId) { color.getContrastedColor() }
            IconSelectionContainer(
                containerLabel = stringResource(R.string.label_icon),
                selectedIcon = icon,
                firstLetter = nameFirstLetter,
                color = color,
                iconColor = iconColor,
                onSelected = { labelIcon -> onEvent(LabelScreenEvent.IconChanged(labelIcon)) }
            )
            //TODO Notifications
        }
    }

}