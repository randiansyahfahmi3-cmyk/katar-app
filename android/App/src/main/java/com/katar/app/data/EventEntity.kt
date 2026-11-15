package com.katar.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * EventEntity - Entitas kegiatan/agenda KATAR.
 * Menyimpan detail kegiatan yang akan diadakan.
 */
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "nama_kegiatan") val nama_kegiatan: String,
    @ColumnInfo(name = "deskripsi") val deskripsi: String?,
    @ColumnInfo(name = "lokasi") val lokasi: String?,
    @ColumnInfo(name = "tanggal_waktu") val tanggal_waktu: String,  // Format: yyyy-MM-dd HH:mm atau timestamp
    @ColumnInfo(name = "status") val status: String  // "AKAN_DATANG" or "SELESAI"
)