package com.kuzepa.mydates.feature.home.event

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.AlertDialogContent
import com.kuzepa.mydates.domain.model.Label
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.model.TextFieldMaxLength
import com.kuzepa.mydates.feature.home.event.components.EventDateField
import com.kuzepa.mydates.feature.home.event.components.EventImageChooser
import com.kuzepa.mydates.feature.home.event.components.EventLabelContainer
import com.kuzepa.mydates.ui.components.MyDatesCheckbox
import com.kuzepa.mydates.ui.components.MyDatesExposedDropDown
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorContentBox
import com.kuzepa.mydates.ui.components.baseeditor.BaseEditorScreen
import com.kuzepa.mydates.ui.components.baseeditor.HandleEditorResults
import com.kuzepa.mydates.ui.components.textfield.MyDatesTextField
import com.kuzepa.mydates.ui.navigation.NavigationResult
import com.kuzepa.mydates.ui.navigation.NavigationResultData
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import kotlinx.coroutines.android.awaitFrame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventScreen(
    viewModel: EventViewModel = hiltViewModel(),
    eventId: Int?,
    onNavigateBack: () -> Unit,
    onNavigateToEventTypeCreator: () -> Unit,
    eventTypeNavigationResult: NavigationResultData,
    labelNavigationResult: NavigationResultData,
    removeNavigationResult: (navigationKey: String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var showGoBackDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (state.isNewEvent) {
            // Request focus when the dialog appears
            awaitFrame()
            focusRequester.requestFocus()
        }
    }

    HandleEditorResults(
        savingFlow = viewModel.savingFlow,
        deletingFlow = viewModel.deletingFlow,
        onSuccess = { onNavigateBack() },
        onError = { /* TODO show error */ }
    )

    BaseEditorScreen(
        title = stringResource(
            if (state.isNewEvent) R.string.event_creator_title else R.string.event_editor_title
        ),
        isNewItem = state.isNewEvent,
        hasChanges = state.hasChanges,
        onNavigateBack = {
            showGoBackDialog = false
            onNavigateBack()
        },
        onSave = { viewModel.onEvent(EventScreenEvent.Save) },
        onDelete = {
            viewModel.onEvent(EventScreenEvent.Delete)
            showDeleteDialog = false
        },
        showGoBackDialog = showGoBackDialog,
        showDeleteDialog = showDeleteDialog,
        onShowGoBackDialogChange = { showGoBackDialog = it },
        onShowDeleteDialogChange = { showDeleteDialog = it },
        deleteDialogContent = AlertDialogContent(
            title = context.getString(
                R.string.delete_dialog_title_pattern,
                stringResource(R.string.this_event)
            ),
            message = context.getString(
                R.string.delete_dialog_msg_pattern,
                stringResource(R.string.this_event)
            ),
            positiveButtonText = stringResource(R.string.button_delete),
            negativeButtonText = stringResource(R.string.button_cancel),
            icon = Icons.Default.Delete
        ),
        scrollBehavior = scrollBehavior,
    ) {
        EventScreenContent(
            onEvent = { viewModel.onEvent(it) },
            dateMask = viewModel.getDateMask(),
            dateDelimiter = viewModel.getMaskDelimiter(),
            eventTypes = state.availableEventTypes.map { it.name },
            onNavigateToEventType = onNavigateToEventTypeCreator,
            focusRequester = focusRequester,
            state = state
        )
    }

    LaunchedEffect(eventTypeNavigationResult.result) {
        if (eventTypeNavigationResult.result == NavigationResult.OK) {
            viewModel.loadEventTypes()
            viewModel.handleEventTypeResult(eventTypeNavigationResult)
            removeNavigationResult(NavigationResult.EVENT_TYPE_KEY)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventScreenContent(
    onEvent: (EventScreenEvent) -> Unit,
    dateMask: String,
    dateDelimiter: Char,
    eventTypes: List<String>,
    state: EventUiState,
    onNavigateToEventType: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    BaseEditorContentBox(
        addSpacerForFabButton = true,
        modifier = modifier
    ) {
        with(state) {
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
                focusRequester = if (isNewEvent) focusRequester else null,
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
                onAddNewItem = onNavigateToEventType,
                addNewItemLabel = stringResource(R.string.event_type_creator_title),
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(R.string.event_type_spinner_placeholder)
            )
            EventLabelContainer(
                label = stringResource(R.string.labels_title),
                labels = labels,
                onLabelClick = { labelId -> onEvent(EventScreenEvent.LabelClicked(labelId)) },
                onRemoveLabelClick = { labelId ->
                    onEvent(
                        EventScreenEvent.RemoveLabelFromEvent(
                            labelId
                        )
                    )
                },
                buttonRemoveDescription = stringResource(R.string.remove_label_hint),
                addLabelText = stringResource(R.string.button_add_label),
                onAddLabelClick = { onEvent(EventScreenEvent.NewLabelClicked) },
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
}


@Preview(name = "New event", showSystemUi = true, showBackground = true)
@Composable
fun EventScreenNewEventPreview() {
    MyDatesTheme {
        val focusRequester = remember { FocusRequester() }
        val state = EventUiState(
            name = "Name",
            nameValidationError = "This field is required",
            eventTypeName = "Birthday",
            labels = listOf(
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
        )
        EventScreenContent(
            onEvent = { },
            dateMask = "mm/dd/yyyy",
            dateDelimiter = '/',
            eventTypes = listOf(),
            onNavigateToEventType = {},
            focusRequester = focusRequester,
            state = state
        )
    }
}