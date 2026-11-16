package com.katar.app.data

import kotlinx.coroutines.flow.Flow

class EventRepository(
    private val eventDao: EventDao,
    private val attendanceDao: AttendanceDao,
    private val memberDao: MemberDao
) {

    // === Events ===
    fun getAllEvents(): Flow<List<EventEntity>> = eventDao.getAll()
    fun getAkanDatang(): Flow<List<EventEntity>> = eventDao.getAkanDatang()
    fun getSelesai(): Flow<List<EventEntity>> = eventDao.getSelesai()

    suspend fun insertEvent(event: EventEntity): Long = eventDao.insert(event)
    suspend fun updateEvent(event: EventEntity) = eventDao.insert(event)
    suspend fun deleteEvent(event: EventEntity) = eventDao.delete(event)
    suspend fun getEventById(id: Int): EventEntity? = eventDao.getById(id)

    // === Attendance ===
    fun getAttendanceByEvent(eventId: Int): Flow<List<AttendanceEntity>> =
        attendanceDao.getByEvent(eventId)

    fun countHadir(eventId: Int): Flow<Int> = attendanceDao.countHadir(eventId)
    fun countTotal(eventId: Int): Flow<Int> = attendanceDao.countTotal(eventId)

    suspend fun toggleAttendance(eventId: Int, memberId: Int, hadir: Boolean) {
        val existing = attendanceDao.getOne(eventId, memberId)
        val updated = existing?.copy(
            hadir = hadir,
            waktu_absensi = if (hadir) currentTimestamp() else null
        ) ?: AttendanceEntity(
            event_id = eventId,
            member_id = memberId,
            hadir = hadir,
            waktu_absensi = if (hadir) currentTimestamp() else null
        )
        attendanceDao.insert(updated)
    }

    suspend fun getAttendance(eventId: Int, memberId: Int): Boolean {
        return attendanceDao.getOne(eventId, memberId)?.hadir ?: false
    }

    // === Combined: event + attendance per member ===
    suspend fun getEventsWithAttendance(eventId: Int): List<MemberWithAttendance> {
        val attendance = attendanceDao.getOne(eventId, 0) // dummy untuk trigger
        // Not used in D1
        return emptyList()
    }

    // === Helper: initialize attendance for all members when event created ===
    suspend fun initializeAttendanceForEvent(eventId: Int) {
        // We'll handle this lazily — saat user pertama kali buka detail,
        // kita yang akan insert default "tidak hadir" untuk semua member.
        // Untuk D1, cukup buat placeholder.
    }

    private fun currentTimestamp(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US)
        return sdf.format(java.util.Date())
    }
}

/**
 * Data class untuk kombinasi Member + status hadir.
 * Digunakan di AgendaDetailScreen.
 */
data class MemberWithAttendance(
    val member: MemberEntity,
    val hadir: Boolean
)