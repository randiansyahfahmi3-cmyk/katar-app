package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventViewModel(private val repo: EventRepository) : ViewModel() {
    val events = repo.allEvents.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun add(event: EventEntity) = viewModelScope.launch { repo.insert(event) }
}