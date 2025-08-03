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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.common.composable.getMyDatesUnfocusedContainerColor
import com.kuzepa.mydates.ui.theme.MyDatesTheme

@Composable
internal fun EventImageChooser(
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
            .background(getMyDatesUnfocusedContainerColor())
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