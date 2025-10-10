package com.kuzepa.mydates.feature.imagecropper

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class ImageCropperUiState(
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val savedImagePath: String? = null,
    val error: String? = null
)