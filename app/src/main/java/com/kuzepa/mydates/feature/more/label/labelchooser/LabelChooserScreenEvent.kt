package com.kuzepa.mydates.feature.more.label.labelchooser

import com.kuzepa.mydates.domain.model.label.Label

sealed class LabelChooserScreenEvent {
    data class LabelSelected(val label: Label) : LabelChooserScreenEvent()

    data object Save : LabelChooserScreenEvent()
}