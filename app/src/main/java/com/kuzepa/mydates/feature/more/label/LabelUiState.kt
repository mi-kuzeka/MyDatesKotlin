package com.kuzepa.mydates.feature.more.label

import androidx.compose.runtime.Immutable
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.label.LabelIcon
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState

@Immutable
data class LabelUiState(
    val isNewLabel: Boolean = true,
    val label: Label? = null,
    val hasChanges: Boolean = false,
    val name: String = "",
    val nameFirstLetter: String = "",
    val nameValidationError: String? = null,
    val colorId: Int = LabelColor.RED.id,
    val icon: LabelIcon = LabelIcon.FIRST_LETTER,
    val emoji: String = "\uD83D\uDE42", // TODO move to constants
    val notificationState: NotificationFilterState = NotificationFilterState.FILTER_STATE_ON,
    val iconsAreExpanded: Boolean = false,
    val emojiPickerIsShowing: Boolean = false
)