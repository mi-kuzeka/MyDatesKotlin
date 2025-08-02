package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.data.local.database.dao.LabelDao
import com.kuzepa.mydates.domain.model.Label
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DbLabelRepository @Inject constructor(
    private val labelDao: LabelDao
) : LabelRepository {
    override fun getAllLabels(): Flow<List<Label>> {
        TODO("Not yet implemented")
    }
}