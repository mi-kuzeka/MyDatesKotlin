package com.kuzepa.mydates.feature.home.event

import android.graphics.Bitmap
import com.kuzepa.mydates.domain.model.label.Label

sealed class EventScreenEvent {
    data class ImageChanged(val image: Bitmap) : EventScreenEvent()
    data class NameChanged(val name: String) : EventScreenEvent()
    data class DateChanged(val dateInput: String) : EventScreenEvent()
    object DateFieldHasLostFocus : EventScreenEvent()
    data class HideYearChanged(val hideYear: Boolean) : EventScreenEvent()
    data class EventTypeChanged(val eventTypeName: String) : EventScreenEvent()
    data class LabelsChanged(val labels: List<Label>) : EventScreenEvent()
    data class RemoveLabelFromEvent(val labelId: String) : EventScreenEvent()
    data object RotateImageLeft : EventScreenEvent()
    data object RotateImageRight : EventScreenEvent()
    data object DeleteImage : EventScreenEvent()
    object NewLabelClicked : EventScreenEvent()
    data class NotesChanged(val notes: String) : EventScreenEvent()
    object Save : EventScreenEvent()
    object Delete : EventScreenEvent()
}