package com.katar.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CashTransactionDao {

    @Query("SELECT * FROM cash_transactions ORDER BY tanggal DESC, id DESC")
    fun getAll(): Flow<List<CashTransactionEntity>>

    @Query("""SELECT * FROM cash_transactions WHERE tanggal LIKE :bulanPrefix || '%' ORDER BY tanggal DESC, id DESC""")
    fun getByBulan(bulanPrefix: String): Flow<List<CashTransactionEntity>>

    @Query("SELECT COALESCE(SUM(nominal), 0) FROM cash_transactions WHERE tipe = 'PEMASUKAN'")
    fun getTotalPemasukan(): Flow<Double>

    @Query("SELECT COALESCE(SUM(nominal), 0) FROM cash_transactions WHERE tipe = 'PENGELUARAN'")
    fun getTotalPengeluaran(): Flow<Double>

    @Query("""SELECT COALESCE(
    (SELECT SUM(nominal) FROM cash_transactions WHERE tipe = 'PEMASUKAN'), 0
) - COALESCE(
    (SELECT SUM(nominal) FROM cash_transactions WHERE tipe = 'PENGELUARAN'), 0
)""")
    fun getTotalSaldo(): Flow<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tx: CashTransactionEntity)

    @Delete
    suspend fun delete(tx: CashTransactionEntity)
}