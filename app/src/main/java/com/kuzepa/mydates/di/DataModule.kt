package com.kuzepa.mydates.di

import com.kuzepa.mydates.data.repository.DbEventRepository
import com.kuzepa.mydates.data.repository.DbEventTypeRepository
import com.kuzepa.mydates.data.repository.DbLabelRepository
import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.SortOption
import com.kuzepa.mydates.domain.model.label.Label
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.repository.LabelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Singleton
    @Binds
    fun bindEventRepository(
        eventRepository: DbEventRepository
    ): EventRepository

    @Singleton
    @Binds
    fun bindEventTypeRepository(
        eventTypeRepository: DbEventTypeRepository
    ): EventTypeRepository

    @Singleton
    @Binds
    fun bindLabelRepository(
        labelRepository: DbLabelRepository
    ): LabelRepository
}

class FakeEventRepository @Inject constructor() : EventRepository {
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    private val operations = mutableListOf<String>()
    private var shouldThrowError = false
    private var errorToThrow: Exception? = null

    fun addInitialEvents(vararg events: Event) {
        _events.value = events.toList()
    }

    fun clearAllEvents() {
        _events.value = emptyList()
    }

    fun simulateError(error: Exception) {
        shouldThrowError = true
        errorToThrow = error
    }

    fun clearError() {
        shouldThrowError = false
        errorToThrow = null
    }

    override suspend fun upsertEvent(event: Event) {
        operations.add("upsertEvent:id=${event.id}, name=${event.name}")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with upserting event")
        }
        val currentList = _events.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.id == event.id }

        if (existingIndex != -1) {
            // Update existing event
            currentList[existingIndex] = event
        } else {
            // Insert new event
            currentList.add(event)
        }

        _events.value = currentList
    }

    override suspend fun deleteEventById(id: Long) {
        operations.add("deleteEventById:id=$id")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with deleting event")
        }
        val currentList = _events.value.toMutableList()
        val wasRemoved = currentList.removeIf { it.id == id }

        if (wasRemoved) {
            _events.value = currentList
        }
    }

    override fun getAllEvents(sortOption: SortOption): Flow<List<Event>> {
        operations.add("getAllEvents:sortOption=${sortOption.name}")

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with getting all events")
        }
        return _events.map { events ->
            when (sortOption) {
                SortOption.BY_NAME_ASC -> events.sortedBy { it.name.lowercase() }
                SortOption.BY_NAME_DESC -> events.sortedByDescending { it.name.lowercase() }
                SortOption.BY_DATE_ASC -> events.sortedBy { it.notificationDateCode }
                SortOption.BY_DATE_DESC -> events.sortedByDescending { it.notificationDateCode }
                else -> events
            }
        }
    }

    override fun getEventsByMonth(
        month: Int,
        sortOption: SortOption
    ): Flow<List<Event>> {
        operations.add("getEventsByMonth:month=$month,sortOption=${sortOption.name}")

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with getting events by month")
        }
        return _events.map { events ->
            val filteredEvents = events.filter { it.date.month == month }
            when (sortOption) {
                SortOption.BY_NAME_ASC -> filteredEvents.sortedBy { it.name.lowercase() }
                SortOption.BY_NAME_DESC -> filteredEvents.sortedByDescending { it.name.lowercase() }
                SortOption.BY_DATE_ASC -> filteredEvents.sortedBy { it.notificationDateCode }
                SortOption.BY_DATE_DESC -> filteredEvents.sortedByDescending { it.notificationDateCode }
                else -> filteredEvents
            }
        }
    }

    override suspend fun getEventById(id: Long): Event? {
        operations.add("getEventById:id=$id")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with getting event by id")
        }
        return _events.value.find { it.id == id }
    }
}

class FakeEventTypeRepository @Inject constructor() : EventTypeRepository {
    private val _eventTypes = MutableStateFlow<List<EventType>>(emptyList())
    private val operations = mutableListOf<String>()
    private var shouldThrowError = false
    private var errorToThrow: Exception? = null

    fun addInitialEventTypes(vararg eventTypes: EventType) {
        _eventTypes.value = eventTypes.toList()
    }

    fun clearAllEventTypes() {
        _eventTypes.value = emptyList()
    }

    fun simulateError(error: Exception) {
        shouldThrowError = true
        errorToThrow = error
    }

    fun clearError() {
        shouldThrowError = false
        errorToThrow = null
    }

    override suspend fun upsertEventType(eventType: EventType) {
        operations.add("add:id=${eventType.id}, name=${eventType.name}")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with upserting event type")
        }
        val currentList = _eventTypes.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.id == eventType.id }

        if (existingIndex != -1) {
            // Update existing event type
            currentList[existingIndex] = eventType
        } else {
            // Insert new event type
            currentList.add(eventType)
        }

        _eventTypes.value = currentList
    }

    override suspend fun clearDefaultAndUpsertEventType(eventType: EventType) {
        operations.add("clearDefaultAndUpsertEventType:id=${eventType.id}, name=${eventType.name}")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with clearing default event type")
        }
        val updatedList = _eventTypes.value.map { type ->
            type.copy(isDefault = false)
        }
        _eventTypes.value = updatedList
        upsertEventType(eventType)
    }

    override suspend fun deleteEventTypeById(id: String) {
        operations.add("deleteEventTypeById:id=$id")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with deleting event type")
        }
        val currentList = _eventTypes.value.toMutableList()
        val wasRemoved = currentList.removeIf { it.id == id }

        if (wasRemoved) {
            _eventTypes.value = currentList
        }
    }

    override suspend fun getEventTypeById(id: String): EventType? {
        operations.add("getEventTypeById:id=$id")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with getting event type by id")
        }
        return _eventTypes.value.find { it.id == id }
    }

    override suspend fun getDefaultEventType(): EventType? {
        operations.add("getDefaultEventType")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with getting default event type")
        }
        return _eventTypes.value.find { it.isDefault }
    }

    override fun getAllEventTypes(): Flow<List<EventType>> {
        operations.add("getAllEventTypes")

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with getting all event types")
        }
        return _eventTypes.map { it.sortedBy { eventType -> eventType.name.lowercase() } }
    }
}

class FakeLabelRepository @Inject constructor() : LabelRepository {
    private val _labels = MutableStateFlow<List<Label>>(emptyList())
    private val operations = mutableListOf<String>()
    private var shouldThrowError = false
    private var errorToThrow: Exception? = null

    fun addInitialLabes(vararg labels: Label) {
        _labels.value = labels.toList()
    }

    fun clearAllLabels() {
        _labels.value = emptyList()
    }

    fun simulateError(error: Exception) {
        shouldThrowError = true
        errorToThrow = error
    }

    fun clearError() {
        shouldThrowError = false
        errorToThrow = null
    }

    override suspend fun upsertLabel(label: Label) {
        operations.add("upsertLabel:id=${label.id}, name=${label.name}")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with upserting label")
        }
        val currentList = _labels.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.id == label.id }

        if (existingIndex != -1) {
            // Update existing label
            currentList[existingIndex] = label
        } else {
            // Insert new label
            currentList.add(label)
        }

        _labels.value = currentList
    }

    override suspend fun getLabelById(id: String): Label? {
        operations.add("getLabelById:id=$id")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with getting label by id")
        }
        return _labels.value.find { it.id == id }
    }

    override suspend fun deleteLabelById(id: String) {
        operations.add("deleteLabelById:id=$id")
        delay(50)

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with deleting label")
        }
        val currentList = _labels.value.toMutableList()
        val wasRemoved = currentList.removeIf { it.id == id }

        if (wasRemoved) {
            _labels.value = currentList
        }
    }

    override fun getAllLabels(): Flow<List<Label>> {
        operations.add("getAllLabels")

        if (shouldThrowError) {
            throw errorToThrow ?: RuntimeException("Error with getting all labels")
        }
        return _labels.map { it.sortedBy { label -> label.name.lowercase() } }
    }
}