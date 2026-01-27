package com.kuzepa.mydates.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.RESTRICT
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "events",
    indices = [Index("event_type_id")],
    foreignKeys = [
        ForeignKey(
            entity = EventTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_type_id"],
            onDelete = RESTRICT
        )
    ]
)
data class EventEntity(
    @PrimaryKey
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    val id: Long?,
    @ColumnInfo(name = "name", defaultValue = "")
    val name: String,
    @ColumnInfo(name = "month")
    val month: Int,
    @ColumnInfo(name = "day")
    val day: Int,
    @ColumnInfo(name = "year")
    val year: Int,
    @ColumnInfo(name = "notes", defaultValue = "")
    val notes: String,
    @ColumnInfo(name = "event_type_id")
    val eventTypeId: String,
    @ColumnInfo(name = "image")
    val image: ByteArray?,
    @ColumnInfo(name = "notification_date", defaultValue = "0")
    val notificationDate: Int
    // If you're adding a new field then add to the methods equals and hashCode
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventEntity

        if (id != other.id) return false
        if (month != other.month) return false
        if (day != other.day) return false
        if (year != other.year) return false
        if (notificationDate != other.notificationDate) return false
        if (name != other.name) return false
        if (notes != other.notes) return false
        if (eventTypeId != other.eventTypeId) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + month
        result = 31 * result + day
        result = 31 * result + year
        result = 31 * result + notificationDate
        result = 31 * result + name.hashCode()
        result = 31 * result + notes.hashCode()
        result = 31 * result + eventTypeId.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}