package com.kuzepa.mydates.feature.more.label.labelchooser

import com.kuzepa.mydates.domain.model.label.Label

data class LabelChooserUiState(
    val labels: List<Label> = emptyList(),
    val selectedLabel: Label? = null
)
