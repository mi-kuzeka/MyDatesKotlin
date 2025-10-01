package com.kuzepa.mydates.feature.imagecropper

import android.net.Uri

data class ImageCropperUiState(
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val savedImagePath: String? = null,
    val error: String? = null
)