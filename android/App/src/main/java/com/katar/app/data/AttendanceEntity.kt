package com.katar.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * AttendanceEntity - Tabel relasi kehadiran anggota di suatu kegiatan.
 *
 * Primary Key = composite (event_id + member_id)
 * - Satu anggota tidak bisa terdaftar hadir 2x di kegiatan yang sama
 * - Jika event dihapus, attendance otomatis terhapus (CASCADE)
 * - Jika member dihapus, attendance otomatis terhapus (CASCADE)
 */
@Entity(
    tableName = "attendance",
    primaryKeys = ["event_id", "member_id"],
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MemberEntity::class,
            parentColumns = ["id"],
            childColumns = ["member_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("event_id"),
        Index("member_id")
    ]
)
data class AttendanceEntity(
    @ColumnInfo(name = "event_id") val event_id: Int,
    @ColumnInfo(name = "member_id") val member_id: Int,
    @ColumnInfo(name = "hadir") val hadir: Boolean = false,
    @ColumnInfo(name = "waktu_absensi") val waktu_absensi: String? = null,
    @ColumnInfo(name = "catatan") val catatan: String? = null
)