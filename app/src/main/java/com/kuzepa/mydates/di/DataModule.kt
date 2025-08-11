package com.kuzepa.mydates.di

import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.model.Label
import com.kuzepa.mydates.domain.model.SortOption
import com.kuzepa.mydates.domain.repository.DbEventRepository
import com.kuzepa.mydates.domain.repository.DbEventTypeRepository
import com.kuzepa.mydates.domain.repository.DbLabelRepository
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.EventTypeRepository
import com.kuzepa.mydates.domain.repository.LabelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
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
    override suspend fun upsertEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEventById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getAllEvents(sortOption: SortOption): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun getEventsByMonth(
        month: Int,
        sortOption: SortOption
    ): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventById(id: Int): Event? {
        TODO("Not yet implemented")
    }
}

class FakeEventTypeRepository @Inject constructor() : EventTypeRepository {
    override suspend fun upsertEventType(eventType: EventType) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEventTypeById(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getEventTypeById(id: String): EventType? {
        TODO("Not yet implemented")
    }

    override suspend fun getDefaultEventType(): EventType? {
        TODO("Not yet implemented")
    }

    override fun getAllEventTypes(): Flow<List<EventType>> {
        TODO("Not yet implemented")
    }
}

class FakeLabelRepository @Inject constructor() : LabelRepository {
    override fun getAllLabels(): Flow<List<Label>> {
        TODO("Not yet implemented")
    }
}