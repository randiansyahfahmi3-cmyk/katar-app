package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    // === Semua events ===
    val events: StateFlow<List<EventEntity>> = repository.getAllEvents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val akanDatang: StateFlow<List<EventEntity>> = repository.getAkanDatang()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selesai: StateFlow<List<EventEntity>> = repository.getSelesai()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // === Loading & Error ===
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // === Actions ===
    fun addEvent(event: EventEntity, onSuccess: (Int) -> Unit = {}) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val newId = repository.insertEvent(event)
                _errorMessage.value = null
                onSuccess(newId.toInt())
            } catch (e: Exception) {
                _errorMessage.value = "Gagal tambah kegiatan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.updateEvent(event)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Gagal update kegiatan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteEvent(event)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Gagal hapus kegiatan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getEventById(id: Int): EventEntity? = repository.getEventById(id)

    fun toggleAttendance(eventId: Int, memberId: Int, hadir: Boolean) {
        viewModelScope.launch {
            try {
                repository.toggleAttendance(eventId, memberId, hadir)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal update absensi: ${e.message}"
            }
        }
    }

    fun getAttendanceByEvent(eventId: Int) = repository.getAttendanceByEvent(eventId)

    fun clearError() {
        _errorMessage.value = null
    }
}