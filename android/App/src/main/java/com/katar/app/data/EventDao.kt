package com.katar.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY tanggal_waktu DESC")
    fun getAll(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE status = 'AKAN_DATANG' ORDER BY tanggal_waktu ASC")
    fun getAkanDatang(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE status = 'SELESAI' ORDER BY tanggal_waktu DESC")
    fun getSelesai(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getById(id: Int): EventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity): Long

    @Delete
    suspend fun delete(event: EventEntity)
}