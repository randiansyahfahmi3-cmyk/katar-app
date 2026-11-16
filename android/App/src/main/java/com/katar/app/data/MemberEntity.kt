package com.katar.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nama") val nama: String,
    @ColumnInfo(name = "no_anggota") val no_anggota: String?,
    @ColumnInfo(name = "jabatan") val jabatan: String?,
    @ColumnInfo(name = "rt") val rt: String?,
    @ColumnInfo(name = "rw") val rw: String?,
    @ColumnInfo(name = "no_hp") val no_hp: String?,
    @ColumnInfo(name = "status_aktif") val status_aktif: Boolean
)