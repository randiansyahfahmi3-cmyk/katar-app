package com.katar.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Query("SELECT * FROM attendance WHERE event_id = :eventId")
    fun getByEvent(eventId: Int): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE event_id = :eventId AND member_id = :memberId")
    suspend fun getOne(eventId: Int, memberId: Int): AttendanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attendance: AttendanceEntity)

    @Query("DELETE FROM attendance WHERE event_id = :eventId")
    suspend fun deleteByEvent(eventId: Int)

    @Query("SELECT COUNT(*) FROM attendance WHERE event_id = :eventId AND hadir = 1")
    fun countHadir(eventId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM attendance WHERE event_id = :eventId")
    fun countTotal(eventId: Int): Flow<Int>
}