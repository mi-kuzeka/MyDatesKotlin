package com.kuzepa.mydates.feature.home.event.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.model.AlertDialogContent
import com.kuzepa.mydates.ui.components.baseeditor.DeleteConfirmationDialog
import com.kuzepa.mydates.ui.theme.MyDatesColors
import com.kuzepa.mydates.ui.theme.MyDatesTheme
import com.kuzepa.mydates.ui.theme.Shapes

@Composable
internal fun EventImageChooser(
    image: Bitmap?,
    chooseImage: () -> Unit,
    rotateLeft: () -> Unit,
    rotateRight: () -> Unit,
    removeImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    Crossfade(
        targetState = image,
        animationSpec = tween(200)
    ) { animatedImage ->
        Box(
            modifier = modifier
                .size(
                    dimensionResource(R.dimen.event_image_chooser_size)
                )
                .clip(shape = Shapes.imageBoxShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            if (image == null || animatedImage == null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(all = dimensionResource(R.dimen.padding_small))
                ) {
                    EmptyEventImage(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = stringResource(R.string.choose_image),
                        color = MyDatesColors.accentTextColor,
                        modifier = Modifier
                            .clickable(onClick = chooseImage)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            } else {
                EventImage(image = animatedImage)
                ImageActionButtonsPanel(
                    replaceImage = chooseImage,
                    rotateLeft = rotateLeft,
                    rotateRight = rotateRight,
                    removeImage = {
                        showDeleteDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }
        }
    }
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onDismissDialog = { showDeleteDialog = false },
            onDelete = {
                removeImage()
                showDeleteDialog = false
            },
            deleteDialogContent = AlertDialogContent(
                title = context.getString(
                    R.string.delete_dialog_title_pattern,
                    stringResource(R.string.this_image)
                ),
                positiveButtonText = stringResource(R.string.button_delete),
                negativeButtonText = stringResource(R.string.button_cancel),
                icon = Icons.Default.Delete
            )
        )
    }
}

@Composable
internal fun EventImage(image: Bitmap, modifier: Modifier = Modifier) {
    Image(
        bitmap = image.asImageBitmap(),
        contentDescription = stringResource(R.string.event_image_description),
        modifier = modifier.fillMaxSize()
    )
}

@Composable
internal fun EmptyEventImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.empty_list_white),
        contentDescription = stringResource(R.string.choose_image),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        modifier = modifier
    )
}

@Composable
internal fun ImageActionButtonsPanel(
    replaceImage: () -> Unit,
    rotateLeft: () -> Unit,
    rotateRight: () -> Unit,
    removeImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .size(dimensionResource(R.dimen.image_action_button_size))
    val iconModifier = Modifier
        .size(dimensionResource(R.dimen.default_icon_size))
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary.copy(0.6f))
    ) {
        IconButton(
            onClick = removeImage,
            modifier = buttonModifier
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
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
                painter = painterResource(R.drawable.ic_add_photo_alternate),
                contentDescription = stringResource(R.string.change_image_hint),
                tint = MaterialTheme.colorScheme.secondaryContainer,
                modifier = iconModifier
            )
        }
        IconButton(
            onClick = rotateLeft,
            modifier = buttonModifier
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_rotate_left),
                contentDescription = stringResource(R.string.rotate_left_hint),
                tint = MaterialTheme.colorScheme.secondaryContainer,
                modifier = iconModifier
            )
        }
        IconButton(
            onClick = rotateRight,
            modifier = buttonModifier
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_rotate_right),
                contentDescription = stringResource(R.string.rotate_right_hint),
                tint = MaterialTheme.colorScheme.secondaryContainer,
                modifier = iconModifier
            )
        }
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