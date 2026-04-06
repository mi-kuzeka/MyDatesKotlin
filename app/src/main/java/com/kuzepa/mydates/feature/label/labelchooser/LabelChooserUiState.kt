package com.kuzepa.mydates.feature.label.labelchooser

import com.kuzepa.mydates.domain.model.label.Label

data class LabelChooserUiState(
    val labels: List<Label> = emptyList(),
    val selectedLabel: Label? = null,
    val selectedLabelId: String? = null,
    val errorMessage: String? = null,
)
