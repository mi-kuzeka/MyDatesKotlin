package com.kuzepa.mydates.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kuzepa.mydates.di.FakeEventTypeRepository
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.notification.NotificationFilterState
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
class EventTypeRepositoryTest {
    private fun getTestEventType(
        id: String = UUID.randomUUID().toString(),
        name: String = "Test Event Type",
        isDefault: Boolean = false,
        notificationState: NotificationFilterState = NotificationFilterState.FILTER_STATE_ON,
        showZodiac: Boolean = true
    ): EventType {
        return EventType(
            id = id,
            name = name,
            isDefault = isDefault,
            notificationState = notificationState,
            showZodiac = showZodiac
        )
    }

    @Test
    fun upsertEventTypeShouldAddNewEventTypeWhenNotExists() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val newEventType = getTestEventType(name = "Birthday")

        // When
        repository.upsertEventType(newEventType)

        // Then
        val result = repository.getAllEventTypes().first().firstOrNull()
        assertEquals(newEventType, result)
    }

    @Test
    fun upsertEventTypeShouldUpdateExistingEventTypeWhenExists() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val eventTypeId = UUID.randomUUID().toString()
        val initialEventType = getTestEventType(id = eventTypeId, name = "Old Name")
        val updatedEventType = getTestEventType(id = eventTypeId, name = "New Name")
        repository.addInitialEventTypes(initialEventType)

        // When
        repository.upsertEventType(updatedEventType)

        // Then
        val result = repository.getAllEventTypes().first()
        assertEquals(1, result.size)
        assertEquals("New Name", result[0].name)
        assertEquals(updatedEventType, result[0])
    }

    @Test
    fun clearDefaultAndUpsertEventTypeShouldClearAllDefaultsAndUpsert() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val defaultEventType1 = getTestEventType(id = "1", name = "Default 1", isDefault = true)
        val defaultEventType2 = getTestEventType(id = "2", name = "Default 2", isDefault = true)
        val nonDefaultEventType =
            getTestEventType(id = "3", name = "Non Default", isDefault = false)
        val newDefaultEventType =
            getTestEventType(id = "4", name = "New Default", isDefault = true)

        repository.addInitialEventTypes(defaultEventType1, defaultEventType2, nonDefaultEventType)

        // When
        repository.clearDefaultAndUpsertEventType(newDefaultEventType)

        // Then
        val result = repository.getAllEventTypes().first()
        assertEquals(4, result.size)

        // Check that all previous defaults are cleared
        assertFalse(result.find { it.id == "1" }?.isDefault ?: true)
        assertFalse(result.find { it.id == "2" }?.isDefault ?: true)
        assertFalse(result.find { it.id == "3" }?.isDefault ?: true)

        // Check that new event type is added and is default
        val newType = result.find { it.id == "4" }
        assertEquals("New Default", newType?.name)
        assertTrue(newType?.isDefault ?: false)
    }

    @Test
    fun deleteEventTypeByIdShouldRemoveExistingEventType() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val eventType1 = getTestEventType(id = "1", name = "Type 1")
        val eventType2 = getTestEventType(id = "2", name = "Type 2")
        repository.addInitialEventTypes(eventType1, eventType2)

        // When
        repository.deleteEventTypeById("1")

        // Then
        val result = repository.getAllEventTypes().first()
        assertEquals(1, result.size)
        assertEquals(eventType2, result[0])
        assertFalse(result.any { it.id == "1" })
    }

    @Test
    fun deleteEventTypeByIdShouldDoNothingWhenEventTypeNotFound() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val eventType = getTestEventType(id = "1", name = "Type 1")
        repository.addInitialEventTypes(eventType)
        val initialCount = repository.getAllEventTypes().first().size

        // When
        repository.deleteEventTypeById("999") // Non-existent ID

        // Then
        val result = repository.getAllEventTypes().first()
        assertEquals(initialCount, result.size)
        assertTrue(result.contains(eventType))
    }

    @Test
    fun getEventTypeByIdShouldReturnCorrectEventType() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val targetEventType = getTestEventType(id = "target", name = "Target Type")
        val otherEventType = getTestEventType(id = "other", name = "Other Type")
        repository.addInitialEventTypes(targetEventType, otherEventType)

        // When
        val result = repository.getEventTypeById("target")

        // Then
        assertEquals(targetEventType, result)
        assertEquals("Target Type", result?.name)
    }

    @Test
    fun getEventTypeByIdShouldReturnNullWhenEventTypeNotFound() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val eventType = getTestEventType(id = "1")
        repository.addInitialEventTypes(eventType)

        // When
        val result = repository.getEventTypeById("999")

        // Then
        assertNull(result)
    }

    @Test
    fun getDefaultEventTypeShouldReturnDefaultEventType() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val nonDefaultType = getTestEventType(id = "1", name = "Non Default", isDefault = false)
        val defaultType = getTestEventType(id = "2", name = "Default", isDefault = true)
        val anotherNonDefaultType =
            getTestEventType(id = "3", name = "Another Non Default", isDefault = false)
        repository.addInitialEventTypes(nonDefaultType, defaultType, anotherNonDefaultType)

        // When
        val result = repository.getDefaultEventType()

        // Then
        assertEquals(defaultType, result)
        assertEquals("Default", result?.name)
        assertTrue(result?.isDefault ?: false)
    }

    @Test
    fun getDefaultEventTypeShouldReturnNullWhenNoDefaultExists() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val nonDefaultType1 =
            getTestEventType(id = "1", name = "Non Default 1", isDefault = false)
        val nonDefaultType2 =
            getTestEventType(id = "2", name = "Non Default 2", isDefault = false)
        repository.addInitialEventTypes(nonDefaultType1, nonDefaultType2)

        // When
        val result = repository.getDefaultEventType()

        // Then
        assertNull(result)
    }

    @Test
    fun getAllEventTypesShouldReturnAllEventTypesSortedByName() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val eventType1 = getTestEventType(id = "1", name = "Anniversary")
        val eventType2 = getTestEventType(id = "2", name = "My event")
        val eventType3 = getTestEventType(id = "3", name = "Birthday")
        repository.addInitialEventTypes(eventType1, eventType2, eventType3)

        // When
        val result = repository.getAllEventTypes().first()

        // Then
        assertEquals(3, result.size)
        assertEquals("Anniversary", result[0].name)
        assertEquals("Birthday", result[1].name)
        assertEquals("My event", result[2].name)
    }

    @Test
    fun getAllEventTypesShouldReturnEmptyListWhenNoEventTypes() = runTest {
        // Given
        val repository = FakeEventTypeRepository()

        // When
        val result = repository.getAllEventTypes().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun eventTypesFlowShouldUpdateWhenEventTypesChange() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val initialEventType = getTestEventType(id = "1", name = "Initial")
        repository.addInitialEventTypes(initialEventType)

        // Get initial state
        val initialResult = repository.getAllEventTypes().first()
        assertEquals(1, initialResult.size)

        // When - add new event type
        val newEventType = getTestEventType(id = "2", name = "New Type")
        repository.upsertEventType(newEventType)

        // Then - get updated state
        val updatedResult = repository.getAllEventTypes().first()
        assertEquals(2, updatedResult.size)
        assertTrue(updatedResult.any { it.id == "2" })
    }

    @Test
    fun upsertEventTypeShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val eventType = getTestEventType()
        val testError = RuntimeException("Test upsert error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.upsertEventType(eventType)
        }
    }

    @Test
    fun clearDefaultAndUpsertEventTypeShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val eventType = getTestEventType()
        val testError = RuntimeException("Test clear default error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.clearDefaultAndUpsertEventType(eventType)
        }
    }

    @Test
    fun deleteEventTypeByIdShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val eventType = getTestEventType()
        repository.addInitialEventTypes(eventType)
        val testError = RuntimeException("Test delete error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.deleteEventTypeById("1")
        }
    }

    @Test
    fun getEventTypeByIdShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val testError = RuntimeException("Test get by id error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.getEventTypeById("1")
        }
    }

    @Test
    fun getDefaultEventTypeShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val testError = RuntimeException("Test get default error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.getDefaultEventType()
        }
    }

    @Test
    fun getAllEventTypesShouldThrowErrorWhenSimulated() = runTest {
        // Given
        val repository = FakeEventTypeRepository()
        val testError = RuntimeException("Test get all error")
        repository.simulateError(testError)

        // When & Then
        assertFailsWith<RuntimeException> {
            repository.getAllEventTypes().first()
        }
    }
}