package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.domain.model.Label
import kotlinx.coroutines.flow.Flow

interface LabelRepository {
    fun getAllLabels(): Flow<List<Label>>
}