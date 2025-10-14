package com.kuzepa.mydates.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kuzepa.mydates.common.util.labelcolor.LabelColor
import com.kuzepa.mydates.di.FakeEventRepository
import com.kuzepa.mydates.domain.formatter.toNotificationDateCode
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventDate
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.NotificationFilterState
import com.kuzepa.mydates.domain.model.SortOption
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.model.label.LabelIcon
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class EventRepositoryTest {
    private val birthdayTypeId = UUID.randomUUID().toString()
    private fun getBirthdayEventType(): EventType {
        return EventType(
            id = birthdayTypeId,
            name = "Birthday",
            isDefault = true,
            notificationState = NotificationFilterState.FILTER_STATE_ON,
            showZodiac = true
        )
    }

    private fun getTestEvent(
        id: Long = 1,
        name: String = "Test Event",
        month: Int = 1,
        day: Int = 15,
        year: Int = 2024,
        eventType: EventType = getBirthdayEventType(),
        labels: List<Label> = listOf(
            Label(
                id = UUID.randomUUID().toString(),
                name = "Friends",
                color = LabelColor.LIGHT_BLUE.id,
                notificationState = NotificationFilterState.FILTER_STATE_ON,
                iconId = LabelIcon.SATISFIED.id
            )
        )
    ): Event {
        val eventDate = EventDate(month = month, day = day, year = year)
        return Event(
            id = id,
            name = name,
            date = eventDate,
            type = eventType,
            notes = "Some notes",
            labels = labels,
            notificationDateCode = eventDate.toNotificationDateCode()
        )
    }

    @Test
    fun upsertShouldAddNewEventWhenNotExists() = runTest {
        // Given
        val repository = FakeEventRepository()
        val newEvent = getTestEvent()

        // When
        repository.upsertEvent(newEvent)

        // Then
        val result = repository.getAllEvents(SortOption.BY_DATE_ASC).first().firstOrNull()
        assertEquals(newEvent, result)
    }

    @Test
    fun upsertShouldUpdateExistingEventWhenExists() = runTest {
        // Given
        val repository = FakeEventRepository()
        val initialEvent = getTestEvent(id = 1, name = "Old Name")
        val newName = "New Name"
        val updatedEvent = getTestEvent(id = 1, name = newName)

        repository.addInitialEvents(initialEvent)

        // When
        repository.upsertEvent(updatedEvent)

        // Then
        val result = repository.getAllEvents(SortOption.BY_DATE_ASC).first()
        assertEquals(1, result.size)
        assertEquals(newName, result[0].name)
        assertEquals(updatedEvent, result[0])
    }

    @Test
    fun deleteEventByIdShouldRemoveExistingEvent() = runTest {
        // Given
        val repository = FakeEventRepository()
        val event1 = getTestEvent(id = 1, name = "Event 1")
        val event2 = getTestEvent(id = 2, name = "Event 2")
        repository.addInitialEvents(event1, event2)

        // When
        repository.deleteEventById(1)

        // Then
        val result = repository.getAllEvents(SortOption.BY_DATE_ASC).first()
        assertEquals(1, result.size)
        assertEquals(event2, result[0])
        assertFalse(result.any { it.id == 1L })
    }

    @Test
    fun deleteEventByIdShouldDoNothingWhenEventNotFound() = runTest {
        // Given
        val repository = FakeEventRepository()
        val event = getTestEvent(id = 1, name = "Event 1")
        repository.addInitialEvents(event)
        val initialCount = repository.getAllEvents(SortOption.BY_DATE_ASC).first().size

        // When
        repository.deleteEventById(999) // Non-existent ID

        // Then
        val result = repository.getAllEvents(SortOption.BY_DATE_ASC).first()
        assertEquals(initialCount, result.size)
        assertTrue(result.contains(event))
    }

    @Test
    fun getAllEventsShouldReturnAllEventsSortedByNameAsc() = runTest {
        // Given
        val repository = FakeEventRepository()
        val event1 = getTestEvent(id = 1, name = "Charlie")
        val event2 = getTestEvent(id = 2, name = "Alpha")
        val event3 = getTestEvent(id = 3, name = "Bravo")
        repository.addInitialEvents(event1, event2, event3)

        // When
        val result = repository.getAllEvents(SortOption.BY_NAME_ASC).first()

        // Then
        assertEquals(3, result.size)
        assertEquals("Alpha", result[0].name)
        assertEquals("Bravo", result[1].name)
        assertEquals("Charlie", result[2].name)
    }

    @Test
    fun getAllEventsShouldReturnAllEventsSortedByNameDesc() = runTest {
        // Given
        val repository = FakeEventRepository()
        val event1 = getTestEvent(id = 1, name = "Alpha")
        val event2 = getTestEvent(id = 2, name = "Charlie")
        val event3 = getTestEvent(id = 3, name = "Bravo")
        repository.addInitialEvents(event1, event2, event3)

        // When
        val result = repository.getAllEvents(SortOption.BY_NAME_DESC).first()

        // Then
        assertEquals(3, result.size)
        assertEquals("Charlie", result[0].name)
        assertEquals("Bravo", result[1].name)
        assertEquals("Alpha", result[2].name)
    }

    @Test
    fun getAllEventsShouldReturnAllEventsSortedByDateAsc() = runTest {
        // Given
        val repository = FakeEventRepository()
        val eventDate1 = EventDate(month = 10, day = 3) // later date
        val eventDate2 = EventDate(month = 2, day = 25) // earlier date
        val eventDate3 = EventDate(month = 7, day = 1) // middle date
        val event1 = getTestEvent(id = 1, month = eventDate1.month, day = eventDate1.day)
        val event2 = getTestEvent(id = 2, month = eventDate2.month, day = eventDate2.day)
        val event3 = getTestEvent(id = 3, month = eventDate3.month, day = eventDate3.day)
        repository.addInitialEvents(event1, event2, event3)

        // When
        val result = repository.getAllEvents(SortOption.BY_DATE_ASC).first()

        // Then
        assertEquals(3, result.size)
        assertEquals(eventDate2.toNotificationDateCode(), result[0].notificationDateCode)
        assertEquals(eventDate3.toNotificationDateCode(), result[1].notificationDateCode)
        assertEquals(eventDate1.toNotificationDateCode(), result[2].notificationDateCode)
    }

    @Test
    fun getAllEventsShouldReturnAllEventsSortedByDateDesc() = runTest {
        // Given
        val repository = FakeEventRepository()
        val eventDate1 = EventDate(month = 10, day = 3) // later date
        val eventDate2 = EventDate(month = 2, day = 25) // earlier date
        val eventDate3 = EventDate(month = 7, day = 1) // middle date
        val event1 = getTestEvent(id = 1, month = eventDate1.month, day = eventDate1.day)
        val event2 = getTestEvent(id = 2, month = eventDate2.month, day = eventDate2.day)
        val event3 = getTestEvent(id = 3, month = eventDate3.month, day = eventDate3.day)
        repository.addInitialEvents(event1, event2, event3)

        // When
        val result = repository.getAllEvents(SortOption.BY_DATE_DESC).first()

        // Then
        assertEquals(3, result.size)
        assertEquals(eventDate1.toNotificationDateCode(), result[0].notificationDateCode)
        assertEquals(eventDate3.toNotificationDateCode(), result[1].notificationDateCode)
        assertEquals(eventDate2.toNotificationDateCode(), result[2].notificationDateCode)
    }

    @Test
    fun getEventsByMonthShouldReturnFilteredEvents() = runTest {
        // Given
        val repository = FakeEventRepository()
        val januaryEvent = getTestEvent(id = 1, name = "January Event", month = 1)
        val februaryEvent = getTestEvent(id = 2, name = "February Event", month = 2)
        val anotherJanuaryEvent = getTestEvent(id = 3, name = "Another January Event", month = 1)
        repository.addInitialEvents(januaryEvent, februaryEvent, anotherJanuaryEvent)

        // When
        val result = repository.getEventsByMonth(1, SortOption.BY_NAME_ASC).first()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.date.month == 1 })
        assertEquals("Another January Event", result[0].name)
        assertEquals("January Event", result[1].name)
    }

    @Test
    fun getEventsByMonthShouldReturnEmptyListWhenNoEventsInMonth() = runTest {
        // Given
        val repository = FakeEventRepository()
        val event = getTestEvent(id = 1, month = 3)
        repository.addInitialEvents(event)

        // When
        val result = repository.getEventsByMonth(1, SortOption.BY_NAME_ASC).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun getEventByIdShouldReturnCorrectEvent() = runTest {
        // Given
        val repository = FakeEventRepository()
        val targetEvent = getTestEvent(id = 42, name = "Target Event")
        val otherEvent = getTestEvent(id = 99, name = "Other Event")
        repository.addInitialEvents(targetEvent, otherEvent)

        // When
        val result = repository.getEventById(42)

        // Then
        assertEquals(targetEvent, result)
        assertEquals("Target Event", result?.name)
    }

    @Test
    fun getEventByIdShouldReturnNullWhenEventNotFound() = runTest {
        // Given
        val repository = FakeEventRepository()
        val event = getTestEvent(id = 1)
        repository.addInitialEvents(event)

        // When
        val result = repository.getEventById(999)

        // Then
        assertNull(result)
    }

    @Test
    fun getAllEventsShouldReturnEmptyListWhenNoEvents() = runTest {
        // Given
        val repository = FakeEventRepository()

        // When
        val result = repository.getAllEvents(SortOption.BY_DATE_ASC).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun eventsFlowShouldUpdateWhenEventsChange() = runTest {
        // Given
        val repository = FakeEventRepository()
        val initialEvent = getTestEvent(id = 1, name = "Initial")
        repository.addInitialEvents(initialEvent)

        // Get initial state
        val initialResult = repository.getAllEvents(SortOption.BY_DATE_ASC).first()
        assertEquals(1, initialResult.size)

        // When - add new event
        val newEvent = getTestEvent(id = 2, name = "New Event")
        repository.upsertEvent(newEvent)

        // Then - get updated state
        val updatedResult = repository.getAllEvents(SortOption.BY_DATE_ASC).first()
        assertEquals(2, updatedResult.size)
        assertTrue(updatedResult.any { it.id == 2L })
    }

    @Test
    fun upsertEventShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeEventRepository()
        val event = getTestEvent()
        val testError = RuntimeException("Test upsert error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.upsertEvent(event)
        }
    }

    @Test
    fun deleteEventByIdShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeEventRepository()
        val event = getTestEvent()
        repository.addInitialEvents(event)
        val testError = RuntimeException("Test delete error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.deleteEventById(1)
        }
    }

    @Test
    fun getAllEventsShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeEventRepository()
        val testError = RuntimeException("Test get all error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.getAllEvents(SortOption.BY_DATE_ASC).first()
        }
    }

    @Test
    fun getEventByIdShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeEventRepository()
        val testError = RuntimeException("Test get by id error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.getEventById(1)
        }
    }
}