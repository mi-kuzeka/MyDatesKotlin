package com.kuzepa.mydates.di

import com.kuzepa.mydates.domain.model.Event
import com.kuzepa.mydates.domain.model.EventType
import com.kuzepa.mydates.domain.repository.EventRepository
import com.kuzepa.mydates.domain.repository.DbEventRepository
import com.kuzepa.mydates.domain.repository.DbEventTypeRepository
import com.kuzepa.mydates.domain.repository.DbLabelRepository
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
    override suspend fun addEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllEvents(): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventsByMonth(month: Int): Flow<List<Event>> {
        TODO("Not yet implemented")
    }
}

class FakeEventTypeRepository @Inject constructor() : EventTypeRepository {
    override suspend fun addEventType(eventType: EventType) {
        TODO("Not yet implemented")
    }

}

class FakeLabelRepository @Inject constructor() : LabelRepository {

}