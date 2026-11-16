package com.katar.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * CashTransactionEntity - Entitas transaksi kas KATAR.
 * Menyimpan rekaman pemasukan dan pengeluaran kas.
 */
@Entity(tableName = "cash_transactions")
data class CashTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "judul") val judul: String,
    @ColumnInfo(name = "nominal") val nominal: Double,
    @ColumnInfo(name = "tipe") val tipe: String,  // "PEMASUKAN" or "PENGELUARAN"
    @ColumnInfo(name = "kategori") val kategori: String,  // "Iuran", "Donasi", "Operasional", dll
    @ColumnInfo(name = "tanggal") val tanggal: String,  // Format: yyyy-MM-dd atau timestamp
    @ColumnInfo(name = "penanggung_jawab") val penanggung_jawab: String?
)