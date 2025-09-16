package com.kuzepa.mydates.domain.model

import android.graphics.Bitmap
import com.kuzepa.mydates.domain.model.label.Label

data class Event(
    val id: Int = 0,
    val name: String,
    val date: EventDate,
    val type: EventType,
    val notes: String = "",
    val image: Bitmap? = null,
    val labels: List<Label> = emptyList(),
    val notificationDateCode: Int? = null
)