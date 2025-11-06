package com.kuzepa.mydates.feature.imagecropper

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.RotateLeft
import androidx.compose.material.icons.automirrored.outlined.RotateRight
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzepa.mydates.R
import com.kuzepa.mydates.ui.components.FullScreenLoading
import com.kuzepa.mydates.ui.components.dialog.ErrorDialog
import kotlinx.coroutines.launch

@Composable
fun ImageCropperScreen(
    viewModel: ImageCropperViewModel = hiltViewModel(),
    imageUriString: String,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val cropperState = rememberImageCropperState(state.imageUri)

    // Handle success state
    LaunchedEffect(state.savedImagePath) {
        state.savedImagePath?.let { path ->
            onNavigateBack()
        }
    }

    BackHandler(onBack = { onNavigateBack() })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
            )
    ) {
        ImageCropper(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = cropperState
        )

        CropControlsPanel(
            onRotateLeft = { cropperState.rotateLeft() },
            onRotateRight = { cropperState.rotateRight() },
            onCrop = {
                coroutineScope.launch {
                    cropperState.crop()?.let { bitmap ->
                        viewModel.onCropImage(bitmap)
                    }
                }
            },
            onClose = { onNavigateBack() }
        )
    }

    if (state.isLoading) {
        FullScreenLoading()
    }

    state.error?.let { error ->
        ErrorDialog(
            errorMessage = error,
            onDismissRequest = { viewModel.clearError() },
        )
    }
}

@Composable
fun CropControlsPanel(
    onRotateLeft: () -> Unit,
    onRotateRight: () -> Unit,
    onCrop: () -> Unit,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(MaterialTheme.colorScheme.primary)
            .padding(dimensionResource(R.dimen.padding_default))
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Close button
        CropPanelButton(
            onClick = onClose,
            imageVector = Icons.Outlined.Close,
            contentDescription = stringResource(R.string.close_button_hint)
        )

        // Rotation controls
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rotate left
            CropPanelButton(
                onClick = onRotateLeft,
                imageVector = Icons.AutoMirrored.Outlined.RotateLeft,
                contentDescription = stringResource(R.string.rotate_left_hint)
            )

            Spacer(modifier = Modifier.width(32.dp))

            // Rotate right
            CropPanelButton(
                onClick = onRotateRight,
                imageVector = Icons.AutoMirrored.Outlined.RotateRight,
                contentDescription = stringResource(R.string.rotate_right_hint)
            )
        }

        // Crop button
        CropPanelButton(
            onClick = onCrop,
            imageVector = Icons.Outlined.Check,
            contentDescription = stringResource(R.string.button_save)
        )
    }
}

@Composable
fun CropPanelButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(dimensionResource(R.dimen.min_clickable_size))
    ) {
        Icon(
            imageVector = imageVector,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = contentDescription
        )
    }
}