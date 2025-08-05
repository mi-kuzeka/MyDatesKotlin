package com.kuzepa.mydates.ui.activities.home.event.composable

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.TextFieldMaxLength
import com.kuzepa.mydates.ui.activities.home.event.EventScreenEvent
import com.kuzepa.mydates.ui.activities.home.event.EventViewModel
import com.kuzepa.mydates.ui.common.composable.IconDelete
import com.kuzepa.mydates.ui.common.composable.MyDatesCheckbox
import com.kuzepa.mydates.ui.common.composable.MyDatesExposedDropDown
import com.kuzepa.mydates.ui.common.composable.MyDatesTextField
import com.kuzepa.mydates.ui.common.composable.TopBar
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

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBar(
            title = stringResource(titleResourceId),
            canGoBack = true,
            onGoBack = {
                // TODO show confirmation dialog
                onNavigateBack()
            },
            endIcon = {
                IconDelete(
                    onClick = { /* TODO show confirmation dialog and delete event */ },
                    contentDescription = stringResource(R.string.delete_event_button_hint)
                )
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
            eventTypes = viewModel.getAllEventTypes().map { it.name }
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
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(R.dimen.padding_8),
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(dimensionResource(R.dimen.padding_16))
    ) {
        EventImageChooser(
            image,
            chooseImage = {},
            rotateLeft = {},
            rotateRight = {},
            removeImage = {},
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_8)))
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
    }
}


@Preview(name = "New event")
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
            eventTypes = listOf()
        )
    }
}