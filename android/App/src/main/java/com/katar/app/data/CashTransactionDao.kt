package com.katar.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

@Dao
interface CashTransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transaction: CashTransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransaction(transaction: CashTransactionEntity)

    @Query("DELETE FROM cash_transactions")
    suspend fun deleteAllTransactions()

    @Query("DELETE FROM cash_transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Int)

    @Query("SELECT * FROM cash_transactions ORDER BY tanggal DESC")
    Flow<List<CashTransactionEntity>> getAllTransactions()

    @Query("SELECT * FROM cash_transactions WHERE tipe = :type ORDER BY tanggal DESC")
    Flow<List<CashTransactionEntity>> getTransactionsByType(type: String)

    @Query("SELECT * FROM cash_transactions WHERE kategori LIKE :kategori ORDER BY tanggal DESC")
    Flow<List<CashTransactionEntity>> getTransactionsByKategori(kategori: String)

    @Query("SELECT * FROM cash_transactions WHERE tanggal LIKE :bulan ORDER BY tanggal DESC")
    Flow<List<CashTransactionEntity>> getTransactionsByBulan(bulan: String)

    @Query("SELECT * FROM cash_transactions WHERE tipe = 'PEMASUKAN' ORDER BY tanggal DESC")
    Flow<List<CashTransactionEntity>> getPemasukanFlow()

    @Query("SELECT * FROM cash_transactions WHERE tipe = 'PENGELUARAN' ORDER BY tanggal DESC")
    Flow<List<CashTransactionEntity>> getPengeluaranFlow()

    // Query perhitungan total - dalam Flow
    @Query("SELECT SUM(nominal) FROM cash_transactions WHERE tipe = 'PEMASUKAN'")
    Flow<Double> getTotalPemasukanFlow()

    @Query("SELECT SUM(nominal) FROM cash_transactions WHERE tipe = 'PENGELUARAN'")
    Flow<Double> getTotalPengeluaranFlow()

    @Query("SELECT SUM(CASE WHEN tipe = 'PEMASUKAN' THEN nominal ELSE 0 END) FROM cash_transactions")
    Flow<Double> getTotalSaldoFlow()

    @Query("SELECT COUNT(*) FROM cash_transactions WHERE tipe = 'PEMASUKAN'")
    suspend fun countPemasukan(): Int

    @Query("SELECT COUNT(*) FROM cash_transactions WHERE tipe = 'PENGELUARAN'")
    suspend fun countPengeluaran(): Int

    @Query("SELECT * FROM cash_transactions LIMIT 1")
    suspend fun getLatestTransaction(): CashTransactionEntity?
}