package com.kuzepa.mydates.ui.activities.home.event.composable

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.kuzepa.mydates.ui.activities.home.event.DeletingEvent
import com.kuzepa.mydates.ui.activities.home.event.EventScreenEvent
import com.kuzepa.mydates.ui.activities.home.event.EventViewModel
import com.kuzepa.mydates.ui.activities.home.event.SavingEvent
import com.kuzepa.mydates.ui.common.composable.MyDatesAlertDialog
import com.kuzepa.mydates.ui.common.composable.MyDatesCheckbox
import com.kuzepa.mydates.ui.common.composable.MyDatesExposedDropDown
import com.kuzepa.mydates.ui.common.composable.MyDatesTextField
import com.kuzepa.mydates.ui.common.composable.TopAppBar
import com.kuzepa.mydates.ui.common.composable.color.MyDatesColors
import com.kuzepa.mydates.ui.common.composable.icon.IconDelete
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventScreen(
    viewModel: EventViewModel = hiltViewModel(),
    id: Int?,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.savingEvent.collect { event ->
            when (event) {
                is SavingEvent.Success -> onNavigateBack()
                is SavingEvent.Error -> {
                    /* TODO show error*/
                }
            }
        }
    }

    LaunchedEffect(key1 = context) {
        viewModel.deletingEvent.collect { event ->
            when (event) {
                is DeletingEvent.Success -> onNavigateBack()
                is DeletingEvent.Error -> {
                    /* TODO show error*/
                }
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showGoBackConfirmationDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = !showGoBackConfirmationDialog && !showDeleteDialog) {
        // Custom back action when dialog is shown
        if (viewModel.hasChanges.value) {
            showGoBackConfirmationDialog = true
        } else {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(
                    if (id == null) {
                        R.string.event_creator_title
                    } else {
                        R.string.event_editor_title
                    }
                ),
                canGoBack = true,
                onGoBack = {
                    if (viewModel.hasChanges.value) {
                        showGoBackConfirmationDialog = true
                    } else {
                        onNavigateBack()
                    }
                },
                endIcon = {
                    if (!viewModel.isNewEvent()) {
                        IconDelete(
                            onClick = {
                                showDeleteDialog = true
                            },
                            contentDescription = stringResource(R.string.delete_event_button_hint)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxWidth()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(EventScreenEvent.Save) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.save_event_button_hint)
                )
            }
        },
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

            if (showDeleteDialog) {
                MyDatesAlertDialog(
                    dialogIconImageVector = Icons.Default.Delete,
                    iconDescription = "",
                    dialogTitle = "Delete this event?", // TODO replace
                    dialogText = "You can't restore it after deleting", // TODO replace
                    confirmButtonText = "Yes, delete", // TODO replace
                    dismissButtonText = "Cancel", // TODO replace
                    onDismissRequest = {
                        showDeleteDialog = false
                    },
                    onConfirmation = {
                        showDeleteDialog = false
                        viewModel.onEvent(EventScreenEvent.Delete)
                    }
                )
            }

            if (showGoBackConfirmationDialog) {
                MyDatesAlertDialog(
                    dialogTitle = "Discard changes?", // TODO replace
                    dialogText = null, // TODO replace
                    confirmButtonText = "Yes, discard", // TODO replace
                    dismissButtonText = "Continue editing", // TODO replace
                    onDismissRequest = {
                        showGoBackConfirmationDialog = false
                    },
                    onConfirmation = {
                        showGoBackConfirmationDialog = false
                        onNavigateBack()
                    }
                )
            }
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