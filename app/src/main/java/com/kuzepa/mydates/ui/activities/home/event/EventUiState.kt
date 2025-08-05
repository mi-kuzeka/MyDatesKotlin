package com.kuzepa.mydates.ui.activities.home.event

import android.graphics.Bitmap
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.Label

data class EventUiState(
    val event: Event? = null,
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
    val labels: List<Label> = emptyList()
)