package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.domain.model.label.Label
import kotlinx.coroutines.flow.Flow

interface LabelRepository {
    suspend fun upsertLabel(label: Label)
    suspend fun getLabelById(id: String): Label?
    suspend fun deleteLabelById(id: String)
    fun getAllLabels(): Flow<List<Label>>
}