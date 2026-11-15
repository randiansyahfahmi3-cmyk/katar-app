package com.katar.app.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow/sync
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * EventRepository - Repository layer for Event/Agenda management.
 * Menyediakan Flow reaktif untuk daftar agenda, status kehadiran, dan riwayat.
 */
class EventRepository(private val eventDao: EventDao, private val memberDao: MemberDao) {

    /** Get all events as Flow */
    val allEvents: Flow<List<EventEntity>> = eventDao.getAllEvents()

    /** Get events status AKAN_DATANG */
    val akanDatangFlow: Flow<List<EventEntity>> = eventDao.getAkanDatangFlow()

    /** Get events status SELESAI */
    val selesaiFlow: Flow<List<EventEntity>> = eventDao.getSelesaiFlow()

    /** Get events with attendance count */
    val eventsWithAttendance: Flow<List<EventEntityWithCount>> = eventDao.getEventsWithAttendance()

    /** Search events by nama kegiatan */
    val searchEventsFlow: Flow<(String) -> List<EventEntity>> = { nama ->
        eventDao.searchEvents(nama).awaitFirst()
    }.tupled()

    /** Get specific event by ID */
    suspend fun getEventById(eventId: Int): EventEntity? = eventDao.getEventById(eventId)

    /** Insert new event */
    suspend fun insertEvent(event: EventEntity) {
        eventDao.insertEvent(event)
    }

    /** Update event */
    suspend fun updateEvent(event: EventEntity) {
        eventDao.updateEvent(event)
    }

    /** Delete event and its attendances */
    suspend fun deleteEvent(eventId: Int) {
        eventDao.deleteAttendancesByEventId(eventId)
        eventDao.deleteEventById(eventId)
    }

    /** Toggle attendance for a member at an event */
    suspend fun toggleAttendance(eventId: Int, memberId: Int, hadir: Boolean, catatan: String? = null) {
        val now = java.time.LocalDateTime.now().toString()
        val waktuAbsensi = if (hadir) now else ""
        eventDao.insertAttendance(eventId, memberId, hadir, waktuAbsensi, catatan ?: "")
    }

    /** Get members who attended an event */
    suspend fun getAttendees(eventId: Int): List<MemberEntity> {
        return eventDao.getAttendeesByEvent(eventId).awaitFirst()
    }

    /** Get members who did NOT attend an event */
    suspend fun getAbsentMembers(eventId: Int): List<MemberEntity> {
        val allMembers = memberDao.getAllMembers().awaitFirst()
        val attendees = getAttendees(eventId)
        return allMembers.filter { member ->
            attendees.any { it.id == member.id } == false
        }
    }

    /** Get attendance status of a specific member at an event */
    suspend fun getMemberAttendance(eventId: Int, memberId: Int): Boolean {
        // Query sederhana: cek apakah ada attendance record dengan hadir=1
        val attendances = eventDao.getAllAttendances().awaitFirst()
        return attendances.any { it.eventId == eventId && it.memberId == memberId && it.hadir }
    }

    /** Format tanggal untuk display */
    fun formatTanggal(tanggal: String): String {
        try {
            val sdfInput = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = sdfInput.parse(tanggal)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                return "${calendar.get(Calendar.DAY_OF_MONTH)} ${getBulanName(calendar.get(Calendar.MONTH))} ${calendar.get(Calendar.YEAR)} pukul ${calendar.get(Calendar.HOUR_OF_DISPLAY)}:${calendar.get(Calendar.MINUTE)}"
            }
        } catch (e: Exception) {
            // ignore
        }
        return tanggal
    }

    private fun getBulanName(month: Int): String {
        val bulanArray = arrayOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")
        return bulanArray[month]
    }
}