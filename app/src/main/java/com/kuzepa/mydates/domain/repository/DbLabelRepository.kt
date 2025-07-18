package com.kuzepa.mydates.domain.repository

import com.kuzepa.mydates.data.local.database.dao.LabelDao
import javax.inject.Inject

class DbLabelRepository @Inject constructor(
    private val labelDao: LabelDao
) : LabelRepository {
}