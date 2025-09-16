package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.data.local.database.dao.LabelDao
import com.kuzepa.mydates.domain.mapper.toLabel
import com.kuzepa.mydates.domain.mapper.toLabelEntity
import com.kuzepa.mydates.domain.model.label.Label
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DbLabelRepository @Inject constructor(
    private val labelDao: LabelDao
) : LabelRepository {
    override suspend fun upsertLabel(label: Label) {
        labelDao.upsertLabel(label.toLabelEntity())
    }

    override suspend fun getLabelById(id: String): Label? {
        return labelDao.getLabelById(id)?.toLabel()
    }

    override suspend fun deleteLabelById(id: String) {
        labelDao.deleteLabelById(id)
    }

    override fun getAllLabels(): Flow<List<Label>> {
        return labelDao.getAllLabels().map { labels ->
            labels.map { labelEntity -> labelEntity.toLabel() }
        }
    }
}