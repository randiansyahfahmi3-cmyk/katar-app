package com.katar.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CashTransactionDao {
    @Query("SELECT * FROM cash_transactions ORDER BY tanggal DESC")
    fun getAll(): Flow<List<CashTransactionEntity>>

    @Query("SELECT SUM(nominal) FROM cash_transactions WHERE tipe = 'PEMASUKAN'")
    fun getTotalPemasukan(): Flow<Double?>

    @Query("SELECT SUM(nominal) FROM cash_transactions WHERE tipe = 'PENGELUARAN'")
    fun getTotalPengeluaran(): Flow<Double?>

    @Query("SELECT (SELECT IFNULL(SUM(nominal), 0) FROM cash_transactions WHERE tipe = 'PEMASUKAN') - (SELECT IFNULL(SUM(nominal), 0) FROM cash_transactions WHERE tipe = 'PENGELUARAN')")
    fun getTotalSaldo(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tx: CashTransactionEntity)
}