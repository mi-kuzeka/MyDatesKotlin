package com.kuzepa.mydates.domain.model

import android.graphics.Bitmap

data class Event(
    val id: Int = 0,
    val name: String,
    val date: EventDate,
    val type: EventType,
    val notes: String = "",
    val image: Bitmap? = null,
    val notificationDateCode: Int? = null,
    val labels: List<Label> = emptyList()
)