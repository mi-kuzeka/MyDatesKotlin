package com.kuzepa.mydates.ui.activities.home.event.composable

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.Label
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.model.TextFieldMaxLength
import com.kuzepa.mydates.ui.activities.home.event.EventScreenEvent
import com.kuzepa.mydates.ui.activities.home.event.EventViewModel
import com.kuzepa.mydates.ui.common.baseeditor.BaseEditorContentBox
import com.kuzepa.mydates.ui.common.baseeditor.BaseEditorScreen
import com.kuzepa.mydates.ui.common.baseeditor.HandleEditorResults
import com.kuzepa.mydates.ui.common.composable.MyDatesCheckbox
import com.kuzepa.mydates.ui.common.composable.MyDatesExposedDropDown
import com.kuzepa.mydates.ui.common.composable.MyDatesTextField
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventScreen(
    viewModel: EventViewModel = hiltViewModel(),
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
            if (state.isNewEvent) R.string.event_creator_title else R.string.event_editor_title
        ),
        isNewItem = state.isNewEvent,
        hasChanges = state.hasChanges,
        onNavigateBack = onNavigateBack,
        onSave = { viewModel.onEvent(EventScreenEvent.Save) },
        onDelete = { viewModel.onEvent(EventScreenEvent.Delete) },
        showDeleteDialog = showDeleteDialog,
        showGoBackConfirmationDialog = showGoBackConfirmationDialog,
        onShowDeleteDialogChange = { showDeleteDialog = it },
        onShowGoBackConfirmationDialogChange = { showGoBackConfirmationDialog = it },
        deleteDialogTitle = "Delete this event?", // TODO replace with string resources
        deleteDialogText = "You can't restore it after deleting", // TODO replace with string resources
        scrollBehavior = scrollBehavior
    ) {
        EventScreenContent(
            onEvent = { viewModel.onEvent(it) },
            image = state.image,
            name = state.name,
            nameValidationError = state.nameValidationError,
            date = state.date,
            dateValidationError = state.dateValidationError,
            dateMask = viewModel.getDateMask(),
            dateDelimiter = viewModel.getMaskDelimiter(),
            hideYear = state.hideYear,
            eventTypeName = state.eventTypeName,
            eventTypeValidationError = state.eventTypeValidationError,
            eventTypes = viewModel.getAllEventTypes().map { it.name },
            eventLabels = state.labels,
            notes = state.notes
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventScreenContent(
    onEvent: (EventScreenEvent) -> Unit,
    image: Bitmap?,
    name: String,
    nameValidationError: String?,
    date: String,
    dateValidationError: String?,
    dateMask: String,
    dateDelimiter: Char,
    hideYear: Boolean,
    eventTypeName: String,
    eventTypeValidationError: String?,
    eventTypes: List<String>,
    eventLabels: List<Label>,
    notes: String,
    modifier: Modifier = Modifier
) {
    BaseEditorContentBox(
        modifier = modifier
    ) {
        EventImageChooser(
            image,
            chooseImage = {},
            rotateLeft = {},
            rotateRight = {},
            removeImage = {},
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        MyDatesTextField(
            label = stringResource(R.string.name_label),
            value = name,
            onValueChange = { onEvent(EventScreenEvent.NameChanged(it)) },
            errorMessage = nameValidationError,
            maxLength = TextFieldMaxLength.NAME.length,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )
        EventDateField(
            label = stringResource(R.string.date_label),
            date = date,
            dateMask = dateMask,
            delimiter = dateDelimiter,
            errorMessage = dateValidationError,
            onValueChange = { onEvent(EventScreenEvent.DateChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            checkBox = {
                MyDatesCheckbox(
                    checked = hideYear,
                    onCheckedChange = { onEvent(EventScreenEvent.HideYearChanged(it)) },
                    text = stringResource(R.string.no_year_label),
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        )
        MyDatesExposedDropDown(
            label = stringResource(R.string.event_type_spinner_label),
            value = eventTypeName,
            onValueChange = { onEvent(EventScreenEvent.EventTypeChanged(it)) },
            errorMessage = eventTypeValidationError,
            options = eventTypes,
            onAddNewItem = {
                // TODO show new event type dialog
            },
            addNewItemLabel = stringResource(R.string.event_type_creator_title),
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(R.string.event_type_spinner_placeholder)
        )
        EventLabelContainer(
            label = stringResource(R.string.labels_title),
            labels = eventLabels,
            onLabelClick = { /* TODO show Label Dialog */ },
            onRemoveLabelClick = { /* TODO show confirmation Dialog */ },
            buttonRemoveDescription = stringResource(R.string.remove_label_hint),
            addLabelText = stringResource(R.string.button_add_label),
            onAddLabelClick = { /* TODO show Label Dialog */ },
            modifier = Modifier.fillMaxWidth()
        )
        MyDatesTextField(
            label = stringResource(R.string.notes_edit_title),
            value = notes,
            onValueChange = { onEvent(EventScreenEvent.NotesChanged(it)) },
            maxLength = TextFieldMaxLength.NOTES.length,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            placeholder = stringResource(R.string.notes_edit_hint),
            singleLine = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(name = "New event", showSystemUi = true, showBackground = true)
@Composable
fun EventScreenNewEventPreview() {
    MyDatesTheme {
        EventScreenContent(
            onEvent = { },
            image = null,
            name = "Text",
            nameValidationError = "This firld is required",
            date = "",
            dateMask = "mm/dd/yyyy",
            dateDelimiter = '/',
            dateValidationError = null,
            hideYear = false,
            eventTypeName = "Birthday",
            eventTypeValidationError = null,
            eventTypes = listOf(),
            eventLabels = listOf(
                Label(
                    id = "1",
                    name = "Friends",
                    color = 6,
                    notificationState = NotificationFilterState.FILTER_STATE_ON,
                    iconId = 0
                ),
                Label(
                    id = "2",
                    name = "Family",
                    color = 3,
                    notificationState = NotificationFilterState.FILTER_STATE_ON,
                    iconId = 10
                )
            ),
            notes = ""
        )
    }
}