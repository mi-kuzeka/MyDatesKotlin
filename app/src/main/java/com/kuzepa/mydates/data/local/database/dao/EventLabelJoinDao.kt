package com.kuzepa.mydates.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.kuzepa.mydates.data.local.database.entity.EventLabelJoin
import com.kuzepa.mydates.domain.mapper.toEventLabelJoin

@Dao
interface EventLabelJoinDao {
    @Upsert()
    suspend fun upsert(link: EventLabelJoin)

    @Upsert()
    suspend fun upsertLinks(links: List<EventLabelJoin>)

    @Transaction
    suspend fun syncLinks(isNewEvent: Boolean, eventId: Long, newLabelIds: List<String>) {
        if (!isNewEvent) deleteOrphanedLinks(eventId, newLabelIds)
        upsertLinks(newLabelIds.toEventLabelJoin(eventId))
    }

    @Query("DELETE FROM event_label_join WHERE event_id = :eventId AND label_id NOT IN (:newLabelIds)")
    suspend fun deleteOrphanedLinks(eventId: Long, newLabelIds: List<String>)

    @Delete()
    suspend fun deleteLink(link: EventLabelJoin)

    @Delete()
    suspend fun deleteLinks(links: List<EventLabelJoin>)

    @Query("DELETE FROM event_label_join WHERE event_id = :eventId")
    suspend fun deleteLabelLinksFromEvent(eventId: Long)

    @Query("SELECT * FROM event_label_join WHERE event_id = :eventId")
    suspend fun getLinksForEvent(eventId: Long): List<EventLabelJoin>

    @Query("SELECT * FROM event_label_join WHERE label_id = :labelId")
    suspend fun getLinksForLabel(labelId: String): List<EventLabelJoin>
}