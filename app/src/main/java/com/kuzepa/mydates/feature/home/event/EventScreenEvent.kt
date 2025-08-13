package com.kuzepa.mydates.feature.home.event

import android.graphics.Bitmap
import com.kuzepa.mydates.domain.model.Label

sealed class EventScreenEvent {
    data class ImageChanged(val image: Bitmap) : EventScreenEvent()
    data class NameChanged(val name: String) : EventScreenEvent()
    data class DateChanged(val dateInput: String) : EventScreenEvent()
    object DateFieldHasLostFocus : EventScreenEvent()
    data class HideYearChanged(val hideYear: Boolean) : EventScreenEvent()
    data class EventTypeChanged(val eventTypeName: String) : EventScreenEvent()
    data class LabelsChanged(val labels: List<Label>) : EventScreenEvent()
    data class RemoveLabel(val labelId: String) : EventScreenEvent()
    data class AddLabel(val label: Label) : EventScreenEvent()
    data class NotesChanged(val notes: String) : EventScreenEvent()
    object Save : EventScreenEvent()
    object Delete : EventScreenEvent()
}