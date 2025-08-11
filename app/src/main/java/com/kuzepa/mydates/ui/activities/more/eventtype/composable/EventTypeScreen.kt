package com.kuzepa.mydates.ui.activities.more.eventtype.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.model.TextFieldMaxLength
import com.kuzepa.mydates.ui.activities.more.eventtype.EventTypeScreenEvent
import com.kuzepa.mydates.ui.activities.more.eventtype.EventTypeViewModel
import com.kuzepa.mydates.ui.common.baseeditor.BaseEditorContentBox
import com.kuzepa.mydates.ui.common.baseeditor.BaseEditorScreen
import com.kuzepa.mydates.ui.common.baseeditor.HandleEditorResults
import com.kuzepa.mydates.ui.common.composable.MyDatesTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTypeScreen(
    viewModel: EventTypeViewModel = hiltViewModel(),
    id: Int?,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showGoBackConfirmationDialog by remember { mutableStateOf(false) }

    HandleEditorResults(
        savingFlow = viewModel.savingFlow,
        deletingFlow = viewModel.deletingFlow,
        onSuccess = onNavigateBack,
        onError = { /* TODO show error */ }
    )

    BaseEditorScreen(
        title = stringResource(
            if (state.isNewEventType) R.string.event_type_creator_title else R.string.event_type_editor_title
        ),
        isNewItem = state.isNewEventType,
        hasChanges = state.hasChanges,
        onNavigateBack = onNavigateBack,
        onSave = { viewModel.onEvent(EventTypeScreenEvent.Save) },
        onDelete = { viewModel.onEvent(EventTypeScreenEvent.Delete) },
        showDeleteDialog = showDeleteDialog,
        showGoBackConfirmationDialog = showGoBackConfirmationDialog,
        onShowDeleteDialogChange = { showDeleteDialog = it },
        onShowGoBackConfirmationDialogChange = { showGoBackConfirmationDialog = it },
        deleteDialogTitle = "Delete this event type?", // TODO replace with string resources
        deleteDialogText = "You can't restore it after deleting", // TODO replace with string resources
        scrollBehavior = scrollBehavior
    ) {
        EventTypeScreenContent(
            onEvent = { viewModel.onEvent(it) },
            name = state.name,
            nameValidationError = state.nameValidationError,
            isDefault = state.isDefault,
            showZodiac = state.showZodiac,
            notificationState = state.notificationState,
        )
    }
}

@Composable
fun EventTypeScreenContent(
    onEvent: (EventTypeScreenEvent) -> Unit,
    name: String,
    nameValidationError: String?,
    isDefault: Boolean,
    showZodiac: Boolean,
    notificationState: NotificationFilterState,
    modifier: Modifier = Modifier
) {
    BaseEditorContentBox(
        modifier = modifier
    ) {
        MyDatesTextField(
            label = stringResource(R.string.name_label),
            value = name,
            onValueChange = { onEvent(EventTypeScreenEvent.NameChanged(it)) },
            errorMessage = nameValidationError,
            maxLength = TextFieldMaxLength.NAME.length,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth()
        )

    }
}