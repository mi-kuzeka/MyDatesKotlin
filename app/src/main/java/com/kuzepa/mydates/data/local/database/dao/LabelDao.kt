package com.kuzepa.mydates.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kuzepa.mydates.data.local.database.entity.LabelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelDao {
    @Upsert()
    suspend fun upsertLabel(labelEntity: LabelEntity)

    @Query("SELECT * FROM labels WHERE id = :id;")
    suspend fun getLabelById(id: String): LabelEntity?

    @Query("DELETE FROM labels WHERE id = :id;")
    suspend fun deleteLabelById(id: String)

    @Query("SELECT * FROM labels ORDER BY name ASC;")
    fun getAllLabels(): Flow<List<LabelEntity>>
}