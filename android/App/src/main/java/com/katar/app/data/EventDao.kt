package com.katar.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: Int)

    @Query("DELETE FROM attendances WHERE event_id = :eventId")
    suspend fun deleteAttendancesByEventId(eventId: Int)

    @Query("DELETE FROM attendances WHERE member_id = :memberId")
    suspend fun deleteAttendancesByMemberId(memberId: Int)

    @Query("DELETE FROM attendances")
    suspend fun deleteAllAttendances()

    @Query("INSERT OR REPLACE INTO attendances (event_id, member_id, hadir, waktu_absensi, catatan) VALUES (:eventId, :memberId, :hadir, :waktuAbsensi, :catatan)")
    suspend fun insertAttendance(eventId: Int, memberId: Int, hadir: Boolean, waktuAbsensi: String, catatan: String)

    @Query("SELECT * FROM events ORDER BY tanggal_waktu DESC")
    Flow<List<EventEntity>> getAllEvents()

    @Query("SELECT * FROM events WHERE status = :status ORDER BY tanggal_waktu DESC")
    Flow<List<EventEntity>> getEventsByStatus(status: String)

    @Query("SELECT * FROM events WHERE nama_kegiatan LIKE :nama ORDER BY tanggal_waktu DESC")
    Flow<List<EventEntity>> searchEvents(nama: String)

    @Query("SELECT * FROM events WHERE status = 'AKAN_DATANG' ORDER BY tanggal_waktu ASC")
    Flow<List<EventEntity>> getAkanDatangFlow()

    @Query("SELECT * FROM events WHERE status = 'SELESAI' ORDER BY tanggal_waktu DESC")
    Flow<List<EventEntity>> getSelesaiFlow()

    // Get events with attendance count
    @Query("SELECT e.*, COUNT(a.member_id) as total_hadir FROM events e LEFT JOIN attendances a ON e.id = a.event_id AND a.hadir = 1 GROUP BY e.id ORDER BY e.tanggal_waktu DESC")
    Flow<List<EventEntityWithCount>> getEventsWithAttendance()

    // Get specific event with its attendances
    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEventById(eventId: Int): EventEntity?

    // Get members who attended a specific event
    @Query("SELECT m.* FROM members m JOIN attendances a ON m.id = a.member_id WHERE a.event_id = :eventId AND a.hadir = 1")
    Flow<List<MemberEntity>> getAttendeesByEvent(eventId: Int)
}

// Helper data class for events with attendance count
data class EventEntityWithCount(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "nama_kegiatan") val nama_kegiatan: String,
    @ColumnInfo(name = "deskripsi") val deskripsi: String?,
    @ColumnInfo(name = "lokasi") val lokasi: String?,
    @ColumnInfo(name = "tanggal_waktu") val tanggal_waktu: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "total_hadir") val total_hadir: Long
)