package com.kuzepa.mydates.feature.more.label.labelchooser

import com.kuzepa.mydates.domain.model.label.Label

sealed class LabelChooserScreenEvent {
    data class NewLabelAdded(val label: Label) : LabelChooserScreenEvent()
    data class LabelUpdated(val label: Label) : LabelChooserScreenEvent()
    data class LabelSelected(val label: Label) : LabelChooserScreenEvent()

    object Save : LabelChooserScreenEvent()
}