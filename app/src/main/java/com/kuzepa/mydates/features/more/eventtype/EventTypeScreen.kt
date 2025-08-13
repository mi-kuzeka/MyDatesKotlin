package com.kuzepa.mydates.features.more.eventtype

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.model.TextFieldMaxLength
import com.kuzepa.mydates.ui.common.composable.MyDatesSwitch
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorContentBox
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorDialog
import com.kuzepa.mydates.ui.components.baseeditor.HandleEditorResults
import com.kuzepa.mydates.ui.components.textfield.MyDatesTextField
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTypeScreen(
    viewModel: EventTypeViewModel = hiltViewModel(),
    id: String?,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showGoBackConfirmationDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Request focus when the dialog appears
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    HandleEditorResults(
        savingFlow = viewModel.savingFlow,
        deletingFlow = viewModel.deletingFlow,
        onSuccess = onNavigateBack,
        onError = { /* TODO show error */ }
    )

    BaseEditorDialog(
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
            state = state,
            focusRequester = focusRequester,
        )
    }
}

@Composable
fun EventTypeScreenContent(
    onEvent: (EventTypeScreenEvent) -> Unit,
    state: EventTypeUiState,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    BaseEditorContentBox(
        modifier = modifier
    ) {
        with(state) {
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
                focusRequester = focusRequester,
                modifier = Modifier
                    .fillMaxWidth()
            )
            MyDatesSwitch(
                text = stringResource(R.string.default_event_type_switch),
                checked = isDefault,
                onCheckedChange = { onEvent(EventTypeScreenEvent.IsDefaultChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )
            MyDatesSwitch(
                text = stringResource(R.string.show_zodiac_sign_switch),
                checked = showZodiac,
                onCheckedChange = { onEvent(EventTypeScreenEvent.ShowZodiacChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )
            MyDatesSwitch(
                text = "Show notifications", //TODO replace with string resources
                checked = notificationState == NotificationFilterState.FILTER_STATE_ON,
                onCheckedChange = { onEvent(EventTypeScreenEvent.ShowNotificationsChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}