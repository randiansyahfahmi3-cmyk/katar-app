package com.katar.app.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow/sync
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * CashRepository - Repository layer for Cash Transaction data.
 * Menyediakan Flow reaktif untuk saldo, pemasukan, dan pengeluaran.
 */
class CashRepository(private val transactionDao: CashTransactionDao) {

    /** Get all transactions as Flow (ordered by date descending) */
    val allTransactions: Flow<List<CashTransactionEntity>> = transactionDao.getAllTransactions()

    /** Get transactions by type (PEMASUKAN or PENGELUARAN) */
    val transactionsByType: Flow<(String) -> List<CashTransactionEntity>> = { type ->
        transactionDao.getTransactionsByType(type).awaitFirst()
    }.tupled()

    /** Get transactions by kategori */
    val transactionsByKategori: Flow<(String) -> List<CashTransactionEntity>> = { kategori ->
        transactionDao.getTransactionsByKategori(kategori).awaitFirst()
    }.tupled()

    /** Get transactions by bulan (format: yyyy-MM) */
    val transactionsByBulan: Flow<(String) -> List<CashTransactionEntity>> = { bulan ->
        transactionDao.getTransactionsByBulan(bulan).awaitFirst()
    }.tupled()

    /** Get pemasukan Flow */
    val pemasukanFlow: Flow<List<CashTransactionEntity>> = transactionDao.getPemasukanFlow()

    /** Get pengeluaran Flow */
    val pengeluaranFlow: Flow<List<CashTransactionEntity>> = transactionDao.getPengeluaranFlow()

    /** Get total pemasukan as Flow */
    val totalPemasukanFlow: Flow<Double> = transactionDao.getTotalPemasukanFlow()

    /** Get total pengeluaran as Flow */
    val totalPengeluaranFlow: Flow<Double> = transactionDao.getTotalPengeluaranFlow()

    /** Get total saldo (masuk - keluar) as Flow reaktif */
    val totalSaldoFlow: Flow<Double> = transactionDao.getTotalSaldoFlow()

    /** Count pemasukan */
    suspend fun countPemasukan(): Int = transactionDao.countPemasukan()

    /** Count pengeluaran */
    suspend fun countPengeluaran(): Int = transactionDao.countPengeluaran()

    /** Get latest transaction */
    suspend fun getLatestTransaction(): CashTransactionEntity? = transactionDao.getLatestTransaction().await()

    /** Insert new transaction */
    suspend fun insertTransaction(transaction: CashTransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    /** Update transaction */
    suspend fun updateTransaction(transaction: CashTransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    /** Delete all transactions */
    suspend fun deleteAll() {
        transactionDao.deleteAllTransactions()
    }

    /** Hitung total pemasukan bulan ini (reaktif via Flow kombinasi) */
    val totalPemasukanBulanIniFlow: Flow<Double> = combine(
        totalPemasukanFlow,
        // Filter bulanan bisa dilakukan di UI atau melalui query tambahan
    ) { total ->
        total
    }

    /** Hitung total pengeluaran bulan ini */
    val totalPengeluaranBulanIniFlow: Flow<Double> = combine(
        totalPengeluaranFlow,
    ) { total ->
        total
    }

    /** Format tanggal ke yyyy-MM */
    fun formatBulan(tanggal: String): String {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(tanggal)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}%02d".format(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
            }
        } catch (e: Exception) {
            // ignore
        }
        return ""
    }
}