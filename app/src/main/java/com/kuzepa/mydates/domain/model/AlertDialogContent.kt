package com.kuzepa.mydates.domain.model

import androidx.compose.ui.graphics.painter.Painter

data class AlertDialogContent(
    val title: String,
    val message: String = "",
    val positiveButtonText: String,
    val negativeButtonText: String,
    val icon: Painter? = null,
    val iconDescription: String = "",
)
