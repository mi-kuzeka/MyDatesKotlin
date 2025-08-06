package com.kuzepa.mydates.ui.common.composable.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

internal object MyDatesShapes {
    val labelChipShape: RoundedCornerShape
        get() = RoundedCornerShape(4.dp)

    val defaultDialogShape: RoundedCornerShape
        get() = RoundedCornerShape(8.dp)

    val defaultContainerShape: RoundedCornerShape
        get() = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)

    val imageBoxShape: RoundedCornerShape
        get() = RoundedCornerShape(16.dp)

    val selectedTabShape: RoundedCornerShape
        get() = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
}