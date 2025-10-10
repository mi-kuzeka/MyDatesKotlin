package com.kuzepa.mydates.feature.home.event

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.label.Label

@Immutable
data class EventUiState(
    val isNewEvent: Boolean = true,
    val event: Event? = null,
    val hasChanges: Boolean = false,
    val name: String = "",
    val nameValidationError: String? = null,
    val date: String = "",
    val dateValidationError: String? = null,
    val hideYear: Boolean = false,
    val eventTypeName: String = "",
    val eventTypeValidationError: String? = null,
    val notes: String = "",
    val image: Bitmap? = null,
    /**
     * Label list of current event
     */
    val labels: List<Label> = emptyList(),
    /**
     * Label list for dropdown in LabelChooser dialog
     */
    val dropdownLabels: List<Label> = emptyList(),
    /**
     * All available event types
     */
    val availableEventTypes: List<EventType> = emptyList()
)