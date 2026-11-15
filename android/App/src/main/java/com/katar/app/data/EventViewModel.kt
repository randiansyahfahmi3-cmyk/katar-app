package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flowMutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DecimalFormat

/**
 * EventViewModel - ViewModel layer for Agenda management.
 * Menyediakan state terstruktur untuk AgendaScreen dengan status kehadiran real-time.
 */
class EventViewModel(private val repository: EventRepository) : ViewModel() {

    /** Daftar semua events reaktif */
    private val _allEvents = repository.allEvents
        .stateIn(scope = viewModelScope, initialValue = emptyList(), operator = { it })
    val allEvents: Flow<List<EventEntity>> = _allEvents

    /** Events AKAN_DATANG reaktif */
    private val _akanDatang = repository.akanDatangFlow
        .stateIn(scope = viewModelScope, initialValue = emptyList(), operator = { it })
    val akanDatang: Flow<List<EventEntity>> = _akanDatang

    /** Events SELESAI reaktif */
    private val _selesai = repository.selesaiFlow
        .stateIn(scope = viewModelScope, initialValue = emptyList(), operator = { it })
    val selesai: Flow<List<EventEntity>> = _selesai

    /** Events dengan attendance count */
    private val _eventsWithAttendance = repository.eventsWithAttendance
        .stateIn(scope = viewModelScope, initialValue = emptyList(), operator = { it })
    val eventsWithAttendance: Flow<List<EventEntityWithCount>> = _eventsWithAttendance

    /** Format uang/time */
    private val formatWaktu = DecimalFormat("HH:mm")

    /** State untuk dialog/form tambah event */
    var namaKegiatan by mutableStateOf("")
    var deskripsi by mutableStateOf("")
    var lokasi by mutableStateOf("")
    var tanggalWaktu by mutableStateOf(java.time.LocalDateTime.now().toString())
    var status by mutableStateOf("AKAN_DATANG")

    /** State untuk filter tab */
    var selectedTab by mutableStateOf("akan_datang") // "akan_datang" or "riwayat"

    /** Status warna tab */
    val tabWarna: (String) -> Color
        get() = when (selectedTab) {
            "akan_datang" -> MaterialTheme.colorScheme.tertiary
            "riwayat" -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.onSurface
        }

    /** Count events akan datang */
    val countAkanDatang: Int
        get() = repository.akanDatangFlow.awaitFirst().size

    /** Count events selesai */
    val countSelesai: Int
        get() = repository.selesaiFlow.awaitFirst().size

    /** Insert event baru */
    fun insertEvent() = viewModelScope.launch {
        val event = EventEntity(
            id = 0,
            nama_kegiatan = namaKegiatan,
            deskripsi = deskripsi,
            lokasi = lokasi,
            tanggal_waktu = tanggalWaktu,
            status = status
        )
        repository.insertEvent(event)
        // Reset form
        namaKegiatan = ""
        deskripsi = ""
        lokasi = ""
        tanggalWaktu = java.time.LocalDateTime.now().toString()
        status = "AKAL_DATANG"
    }

    /** Update event */
    fun updateEvent(event: EventEntity) = viewModelScope.launch {
        repository.updateEvent(event)
    }

    /** Delete event */
    fun deleteEvent(eventId: Int) = viewModelScope.launch {
        repository.deleteEvent(eventId)
    }

    /** Toggle attendance member */
    fun toggleAttendance(eventId: Int, memberId: Int, hadir: Boolean, catatan: String? = null) {
        repository.toggleAttendance(eventId, memberId, hadir, catatan)
    }

    /** Get attendees for event */
    fun getAttendees(eventId: Int): List<MemberEntity> {
        return repository.getAttendees(eventId)
    }

    /** Get absent members for event */
    fun getAbsentMembers(eventId: Int): List<MemberEntity> {
        return repository.getAbsentMembers(eventId)
    }

    /** Get member attendance status */
    fun getMemberAttendance(eventId: Int, memberId: Int): Boolean {
        return repository.getMemberAttendance(eventId, memberId)
    }
}