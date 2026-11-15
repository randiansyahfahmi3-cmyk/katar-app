package com.katar.app.data

import kotlinx.coroutines.flow.Flow

class EventRepository(private val dao: EventDao) {
    val allEvents: Flow<List<EventEntity>> = dao.getAll()
    suspend fun insert(event: EventEntity) = dao.insert(event)
}