package com.kuzepa.mydates.domain.model

import android.graphics.Bitmap

data class Event(
    val id: Int = 0,
    val name: String,
    val eventDate: EventDate,
    val eventType: EventType,
    val notes: String = "",
    val image: Bitmap? = null,
    val notificationDate: Int,
    val labels: List<Label> = emptyList()
)