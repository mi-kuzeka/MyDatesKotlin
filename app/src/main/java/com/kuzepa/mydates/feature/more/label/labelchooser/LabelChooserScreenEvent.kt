package com.kuzepa.mydates.feature.more.label.labelchooser

import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.ui.navigation.NavigationResultData

sealed class LabelChooserScreenEvent {
    data class LabelSelected(val label: Label) : LabelChooserScreenEvent()
    data class OnLabelNavigationResult(val navigationResult: NavigationResultData) :
        LabelChooserScreenEvent()

    object Save : LabelChooserScreenEvent()
}