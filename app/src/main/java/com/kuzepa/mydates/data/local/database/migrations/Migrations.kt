package com.kuzepa.mydates.data.local.database.migrations

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID
import kotlin.math.min

internal object Migrations {
    fun getMigration13to32(): Migration =
        object : Migration(13, 32) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("PRAGMA foreign_keys = OFF")
                db.beginTransaction()
                try {
                    db.execSQL(
                        """
                    CREATE TABLE labels_new(
                        id TEXT PRIMARY KEY NOT NULL,
                        name TEXT DEFAULT '' NOT NULL,
                        color INTEGER DEFAULT 1 NOT NULL,
                        notification_state INTEGER DEFAULT 0 NOT NULL,
                        icon_id INTEGER DEFAULT 0 NOT NULL,
                        emoji TEXT DEFAULT '' NOT NULL
                    );
                    """.trimIndent()
                    )
                    db.execSQL(
                        """
                        INSERT INTO labels_new(
                            id, name, color, notification_state, icon_id, emoji) 
                        SELECT 
                            id, name, color, notification_state, icon_id, '' 
                        FROM labels;
                    """.trimIndent()
                    )
                    db.execSQL("DROP TABLE labels;")
                    db.execSQL("ALTER TABLE labels_new RENAME TO labels;")

                    db.execSQL(
                        """
                    CREATE TABLE event_types_new(
                        id TEXT PRIMARY KEY NOT NULL,
                        name TEXT DEFAULT '' NOT NULL,
                        is_default INTEGER DEFAULT 0 NOT NULL,
                        notification_state INTEGER DEFAULT 0 NOT NULL,
                        show_zodiac INTEGER DEFAULT 0 NOT NULL
                    );
                    """.trimIndent()
                    )
                    db.execSQL(
                        """
                        INSERT INTO event_types_new (
                            id, name, is_default, notification_state, show_zodiac) 
                        SELECT 
                            id, name, is_default, notification_state, show_zodiac 
                        FROM event_types;
                    """.trimIndent()
                    )
                    db.execSQL("DROP TABLE event_types;")
                    db.execSQL("ALTER TABLE event_types_new RENAME TO event_types;")

                    db.execSQL(
                        """
                    CREATE TABLE events_new(
                        id INTEGER PRIMARY KEY NOT NULL,
                        name TEXT DEFAULT '' NOT NULL,
                        month INTEGER NOT NULL,
                        day INTEGER NOT NULL,
                        year INTEGER NOT NULL,
                        notes TEXT DEFAULT '' NOT NULL,
                        event_type_id TEXT NOT NULL,
                        image BLOB,
                        notification_date INTEGER DEFAULT 0 NOT NULL,
                        FOREIGN KEY(event_type_id) REFERENCES event_types(id) ON DELETE RESTRICT
                    );
                    """.trimIndent()
                    )

                    val defaultEventTypeId = db.query(
                        "SELECT id FROM event_types LIMIT 1"
                    ).use { cursor ->
                        if (cursor.moveToFirst()) cursor.getString(0) else null
                    }
                    db.execSQL(
                        """
                        INSERT INTO events_new(
                            id, name, month, day, year, notes,
                            event_type_id, image, notification_date) 
                        SELECT 
                            e.id, e.name, e.month, e.day, e.year, e.notes,
                            COALESCE(et.id, '$defaultEventTypeId') as event_type_id,
                            e.image, e.notification_date
                        FROM events e 
                        LEFT JOIN event_types et ON e.event_type_id = et.id;
                    """.trimIndent()
                    )

                    db.execSQL(
                        """
                        CREATE TABLE event_label_join(
                            event_id INTEGER NOT NULL,
                            label_id TEXT NOT NULL,
                            PRIMARY KEY(event_id, label_id),
                            FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE,
                            FOREIGN KEY(label_id) REFERENCES labels(id) ON DELETE CASCADE
                        );
                    """.trimIndent()
                    )

                    val eventsCursor = db.query("SELECT id, labels FROM events;")
                    while (eventsCursor.moveToNext()) {
                        val idColumnIndex = eventsCursor.getColumnIndex("id")
                        val labelsColumnIndex = eventsCursor.getColumnIndex("labels")
                        val eventId = eventsCursor.getInt(idColumnIndex)
                        val labelIdsString = eventsCursor.getString(labelsColumnIndex)

                        if (!labelIdsString.isNullOrEmpty()) {
                            val labelIds = labelIdsString.split(";").filter { it.isNotBlank() }
                            for (labelId in labelIds) {
                                db.execSQL(
                                    """
                                    INSERT OR IGNORE INTO event_label_join(event_id, label_id)
                                    VALUES ($eventId, '$labelId');
                                """.trimIndent()
                                )
                            }
                        }
                    }
                    eventsCursor.close()

                    db.execSQL("DROP TABLE events;")
                    db.execSQL("ALTER TABLE events_new RENAME TO events;")

                    db.execSQL("CREATE INDEX index_events_event_type_id ON events(event_type_id);")
                    db.execSQL("CREATE INDEX index_event_label_join_event_id ON event_label_join(event_id);")
                    db.execSQL("CREATE INDEX index_event_label_join_label_id ON event_label_join(label_id);")

                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Log.e(
                        "MyDatesDatabase",
                        "Error with updating database 13 to 36 (Release 2026.1.0): ${e.message}"
                    )
                    throw e
                } finally {
                    db.endTransaction()
                    db.execSQL("PRAGMA foreign_keys = ON;")
                }
            }
        }

    fun getMigration4to13(birthdayEventTypeName: String): Migration = object : Migration(4, 13) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.beginTransaction()
            try {
                // Creating table "labels"
                db.execSQL(
                    "CREATE TABLE labels" +
                            "  (id TEXT PRIMARY KEY NOT NULL," +
                            " name TEXT," +
                            " color INTEGER NOT NULL," +
                            " notification_state INTEGER DEFAULT 0 NOT NULL," +
                            " icon_id INTEGER DEFAULT 0 NOT NULL);"
                )

                db.execSQL("ALTER TABLE events ADD labels TEXT;")

                // Cut names by max length
                val eventNameMaxLength = 100
                val eventNotesMaxLength = 1000
                val eventsCursor = db.query(
                    "SELECT * FROM events WHERE length(name) > " + eventNameMaxLength +
                            " OR length(notes) > " + eventNotesMaxLength
                )
                while (eventsCursor.moveToNext()) {
                    val idColumnIndex = eventsCursor.getColumnIndex("id")
                    val nameColumnIndex = eventsCursor.getColumnIndex("name")
                    val notesColumnIndex = eventsCursor.getColumnIndex("notes")
                    if (nameColumnIndex >= 0 && notesColumnIndex >= 0) {
                        val eventId = eventsCursor.getInt(idColumnIndex)
                        val oldName = eventsCursor.getString(nameColumnIndex)
                        val newName = oldName.take(min(oldName.length, eventNameMaxLength))
                        val oldNotes = eventsCursor.getString(notesColumnIndex)
                        val newNotes =
                            oldNotes.take(min(oldNotes.length, eventNotesMaxLength))
                        db.execSQL(
                            "UPDATE events SET name = \"" + newName +
                                    "\", notes = \"" + newNotes +
                                    "\" WHERE id = " + eventId + ";"
                        )
                    }
                }
                eventsCursor.close()

                val eventTypeNameMaxLength = 100
                val eventTypesCursor = db.query(
                    "SELECT * FROM event_types WHERE length(name)>$eventTypeNameMaxLength"
                )
                while (eventTypesCursor.moveToNext()) {
                    val idColumnIndex = eventTypesCursor.getColumnIndex("id")
                    val nameColumnIndex = eventTypesCursor.getColumnIndex("name")
                    if (nameColumnIndex >= 0) {
                        val eventTypeId = eventTypesCursor.getString(idColumnIndex)
                        val oldName = eventTypesCursor.getString(nameColumnIndex)
                        val newName =
                            oldName.take(min(oldName.length, eventTypeNameMaxLength))
                        db.execSQL(
                            "UPDATE event_types SET name = \"" + newName +
                                    "\" WHERE id = \"" + eventTypeId + "\";"
                        )
                    }
                }
                eventTypesCursor.close()

                // Creating "notification_state" column in "event_types" table
                db.execSQL("ALTER TABLE event_types ADD notification_state INTEGER DEFAULT 0 NOT NULL;")

                // Creating "show_zodiac" column in "event_types" table
                db.execSQL("ALTER TABLE event_types ADD show_zodiac INTEGER DEFAULT 0 NOT NULL;")
                db.execSQL(
                    "UPDATE event_types SET show_zodiac = 1 WHERE name = \"$birthdayEventTypeName\";"
                )
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.e("MyDatesDatabase", "Error with updating database 4 to 13 (Release 2024.2.0")
                e.printStackTrace()
            } finally {
                db.endTransaction()
            }
        }
    }

    fun getMigration3to4(): Migration = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.beginTransaction()
            try {
                // Creating "is_default" column in "event_types" table
                db.execSQL("ALTER TABLE event_types ADD is_default INTEGER DEFAULT 0 NOT NULL;")
                // Creating "notification_date" column in "events" table
                db.execSQL("ALTER TABLE events ADD notification_date INTEGER DEFAULT 0 NOT NULL;")

                // Set notification_date for events
                val eventsCursor = db.query("SELECT id, month, day FROM events")
                while (eventsCursor.moveToNext()) {
                    val idIndex = eventsCursor.getColumnIndex("id")
                    val monthIndex = eventsCursor.getColumnIndex("month")
                    val dayIndex = eventsCursor.getColumnIndex("day")
                    if (idIndex >= 0 && monthIndex >= 0 && dayIndex >= 0) {
                        val eventId = eventsCursor.getInt(idIndex)
                        val month = eventsCursor.getInt(monthIndex)
                        val day = eventsCursor.getInt(dayIndex)

                        val notificationDate = (month * 100) + day

                        db.execSQL(
                            "UPDATE events SET notification_date = $notificationDate WHERE id = $eventId;"
                        )
                    }
                }
                eventsCursor.close()

                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.e("MyDatesDatabase", "Error with updating database 3 to 4")
                e.printStackTrace()
            } finally {
                db.endTransaction()
            }
        }
    }

    fun getMigration2to3(): Migration = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.beginTransaction()
            try {
                // Update event_type_id from INTEGER to TEXT (UUID)
                db.execSQL(
                    "ALTER TABLE events" +
                            " RENAME COLUMN event_type_id TO event_type_id_old;"
                )
                db.execSQL("ALTER TABLE events ADD event_type_id TEXT DEFAULT \"0\" NOT NULL;")
                db.execSQL(
                    "ALTER TABLE event_types" +
                            " RENAME COLUMN id TO id_old;"
                )
                db.execSQL("ALTER TABLE event_types ADD id TEXT DEFAULT \"0\" NOT NULL;")

                val eventTypesCursor = db.query("SELECT * FROM event_types")
                while (eventTypesCursor.moveToNext()) {
                    val oldIdColumnIndex = eventTypesCursor.getColumnIndex("id_old")
                    if (oldIdColumnIndex >= 0) {
                        val oldEventTypeId = eventTypesCursor.getInt(oldIdColumnIndex)
                        val newEventTypeId: String = UUID.randomUUID().toString()
                        db.execSQL(
                            "UPDATE event_types SET id = \"$newEventTypeId\" WHERE id_old = $oldEventTypeId;"
                        )
                        db.execSQL(
                            "UPDATE events SET event_type_id = \"$newEventTypeId\" WHERE event_type_id_old = $oldEventTypeId;"
                        )
                    }
                }
                eventTypesCursor.close()

                db.execSQL("ALTER TABLE event_types RENAME TO event_types_old;")
                db.execSQL(
                    "CREATE TABLE event_types" +
                            "  (id TEXT PRIMARY KEY NOT NULL," +
                            " name TEXT);"
                )
                db.execSQL(
                    "INSERT INTO event_types(id, name)" +
                            " SELECT id, name FROM event_types_old;"
                )
                db.execSQL("DROP TABLE event_types_old;")

                db.execSQL("ALTER TABLE events RENAME TO events_old;")

                db.execSQL(
                    "CREATE TABLE events" +
                            "  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                            ", name TEXT" +
                            ", month INTEGER NOT NULL" +
                            ", day INTEGER NOT NULL" +
                            ", year INTEGER NOT NULL" +
                            ", notes TEXT" +
                            ", event_type_id TEXT" +
                            ", image BLOB" +
                            ");"
                )


                db.execSQL(
                    "INSERT INTO events(id, name, month, day, year, notes, event_type_id, image)" +
                            " SELECT id, name, month, day, year, notes, event_type_id, image" +
                            " FROM events_old;"
                )
                db.execSQL("DROP TABLE events_old;")

                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.e("MyDatesDatabase", "Error with updating database 2 to 3")
                e.printStackTrace()
            } finally {
                db.endTransaction()
            }
        }
    }

    fun getMigration1to2(): Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.beginTransaction()
            try {
                // Rename categories to event_types
                db.execSQL(
                    "ALTER TABLE events" +
                            " RENAME COLUMN category_id TO event_type_id;"
                )

                db.execSQL(
                    "CREATE TABLE event_types" +
                            "  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            " name TEXT NOT NULL);"
                )

                db.execSQL(
                    "INSERT INTO event_types(id, name)" +
                            " SELECT id, name FROM categories;"
                )

                db.execSQL("DROP TABLE categories;")
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.e("MyDatesDatabase", "Error with updating database 1 to 2")
                e.printStackTrace()
            } finally {
                db.endTransaction()
            }
        }
    }
}