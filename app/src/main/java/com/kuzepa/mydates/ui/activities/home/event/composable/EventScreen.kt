package com.kuzepa.mydates.ui.activities.home.event.composable

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.RotateLeft
import androidx.compose.material.icons.automirrored.outlined.RotateRight
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.TextFieldMaxLength
import com.kuzepa.mydates.ui.activities.home.event.EventScreenEvent
import com.kuzepa.mydates.ui.activities.home.event.EventViewModel
import com.kuzepa.mydates.ui.activities.main.composable.TopBar
import com.kuzepa.mydates.ui.common.composable.IconDelete
import com.kuzepa.mydates.ui.common.composable.MyDatesTextField
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
fun EventScreen(
    viewModel: EventViewModel = hiltViewModel(),
    id: Int?,
    onNavigateBack: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
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
            image = state.value.image,
            name = state.value.name,
            nameValidationError = state.value.nameValidationError,
            date = state.value.date,
            dateValidationError = state.value.dateValidationError,
            dateMask = viewModel.getDateMask(),
            dateDelimiter = viewModel.getMaskDelimiter()
        )
    }
}

@Composable
fun EventScreenContent(
    onEvent: (EventScreenEvent) -> Unit,
    image: Bitmap?,
    name: String,
    nameValidationError: String?,
    date: String,
    dateValidationError: String?,
    dateMask: String,
    dateDelimiter: Char,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(R.dimen.padding_large),
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(dimensionResource(R.dimen.padding_large))
    ) {
        EventImageChooser(
            image,
            chooseImage = {},
            rotateLeft = {},
            rotateRight = {},
            removeImage = {}
        )
        MyDatesTextField(
            label = stringResource(R.string.name_edit_title),
            value = name,
            onValueChange = { onEvent(EventScreenEvent.NameChanged(it)) },
            errorMessage = nameValidationError,
            maxLength = TextFieldMaxLength.NAME.length,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )
        EventDateFieldView(
            label = stringResource(R.string.date_edit_title),
            date = date,
            dateMask = dateMask,
            delimiter = dateDelimiter,
            errorMessage = dateValidationError,
            onValueChange = { onEvent(EventScreenEvent.DateChanged(it)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EventImageChooser(
    image: Bitmap?,
    chooseImage: () -> Unit,
    rotateLeft: () -> Unit,
    rotateRight: () -> Unit,
    removeImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(
                dimensionResource(R.dimen.event_image_chooser_size)
            )
            .clip(shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (image == null) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(all = dimensionResource(R.dimen.padding_medium))
            ) {
                EmptyEventImage(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(R.string.choose_image),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable(onClick = chooseImage)
                        .align(Alignment.CenterHorizontally)
                )
            }
        } else {
            EventImage(image = image)
            ImageActionButtonsPanel(
                replaceImage = chooseImage,
                rotateLeft = rotateLeft,
                rotateRight = rotateRight,
                removeImage = removeImage,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun EventImage(image: Bitmap, modifier: Modifier = Modifier) {
    Image(
        bitmap = image.asImageBitmap(),
        contentDescription = stringResource(R.string.event_image_description),
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun EmptyEventImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.empty_list_white),
        contentDescription = stringResource(R.string.choose_image),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        modifier = modifier
    )
}

@Composable
fun ImageActionButtonsPanel(
    replaceImage: () -> Unit,
    rotateLeft: () -> Unit,
    rotateRight: () -> Unit,
    removeImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .size(dimensionResource(R.dimen.image_action_button_size))
    val iconModifier = Modifier
        .size(dimensionResource(R.dimen.icon_size))
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .alpha(0.5f)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        IconButton(
            onClick = removeImage,
            modifier = buttonModifier
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.rotate_right_hint),
                tint = MaterialTheme.colorScheme.errorContainer,
                modifier = iconModifier
            )
        }
        IconButton(
            onClick = replaceImage,
            modifier = buttonModifier
        ) {
            Icon(
                imageVector = Icons.Outlined.AddPhotoAlternate,
                contentDescription = stringResource(R.string.change_image_hint),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = iconModifier
            )
        }
        IconButton(
            onClick = rotateLeft,
            modifier = buttonModifier
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.RotateLeft,
                contentDescription = stringResource(R.string.rotate_left_hint),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = iconModifier
            )
        }
        IconButton(
            onClick = rotateRight,
            modifier = buttonModifier
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.RotateRight,
                contentDescription = stringResource(R.string.rotate_right_hint),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = iconModifier
            )
        }
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
            dateValidationError = null
        )
    }
}

@Preview
@Composable
fun ImageChooserPreview() {
    MyDatesTheme {
        EventImageChooser(
            image = BitmapFactory.decodeResource(
                LocalContext.current.resources,
                R.drawable.empty_list_white
            ),
            chooseImage = {},
            rotateLeft = {},
            rotateRight = {},
            removeImage = {},
        )
    }
}