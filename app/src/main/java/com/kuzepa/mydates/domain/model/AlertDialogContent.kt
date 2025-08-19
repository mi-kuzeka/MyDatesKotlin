package com.kuzepa.mydates.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class AlertDialogContent(
    val title: String,
    val message: String,
    val positiveButtonText: String,
    val negativeButtonText: String,
    val icon: ImageVector? = null,
    val iconDescription: String = "",
)
