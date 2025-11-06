package com.kuzepa.mydates.domain.model

data class EventDate(
    val month: Int,
    val day: Int,
    val year: Int = -1
)

fun EventDate.hasYear() = year > -1

fun EventDate.getEmptyAge() = -1
