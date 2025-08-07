package com.kuzepa.mydates.ui.activities.home.event.composable

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.kuzepa.mydates.ui.activities.home.event.SavingEvent
import com.kuzepa.mydates.ui.common.composable.IconDelete
import com.kuzepa.mydates.ui.common.composable.MyDatesCheckbox
import com.kuzepa.mydates.ui.common.composable.MyDatesExposedDropDown
import com.kuzepa.mydates.ui.common.composable.MyDatesTextField
import com.kuzepa.mydates.ui.common.composable.TopBar
import com.kuzepa.mydates.ui.common.composable.color.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
internal fun EventScreen(
    viewModel: EventViewModel = hiltViewModel(),
    id: Int?,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val titleResourceId = if (id == null) {
        R.string.event_creator_title
    } else {
        R.string.event_editor_title
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.savingEvent.collect { event ->
            when (event) {
                is SavingEvent.Success -> onNavigateBack()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .background(MyDatesColors.screenBackground)
        ) {
            TopBar(
                title = stringResource(titleResourceId),
                canGoBack = true,
                onGoBack = {
                    // TODO show confirmation dialog
                    onNavigateBack()
                },
                endIcon = {
                    if (state.event != null) {
                        IconDelete(
                            onClick = { /* TODO show confirmation dialog and delete event */ },
                            contentDescription = stringResource(R.string.delete_event_button_hint)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
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
        FloatingActionButton(
            onClick = {
                viewModel.onEvent(EventScreenEvent.Save)
            },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_default))
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(R.string.save_event_button_hint)
            )
        }
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
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(R.dimen.padding_small),
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(dimensionResource(R.dimen.padding_default))
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
                    modifier = Modifier.align(Alignment.End),
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
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.fab_size)))
    }
}


@Preview(name = "New event", showSystemUi = true, showBackground = true)
@Composable
fun EventScreenNewEventPreview() {
    MyDatesTheme {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .background(MyDatesColors.screenBackground)
        ) {
            TopBar(
                title = "New event",
                canGoBack = true,
                onGoBack = { },
                endIcon = {
                    IconDelete(
                        onClick = { /* TODO show confirmation dialog and delete event */ },
                        contentDescription = stringResource(R.string.delete_event_button_hint)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
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
}