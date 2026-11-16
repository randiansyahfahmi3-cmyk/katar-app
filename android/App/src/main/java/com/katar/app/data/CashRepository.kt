package com.katar.app.data

import kotlinx.coroutines.flow.Flow

class CashRepository(private val dao: CashTransactionDao) {

    fun getAll(): Flow<List<CashTransactionEntity>> = dao.getAll()

    fun getByBulan(bulanPrefix: String): Flow<List<CashTransactionEntity>> = dao.getByBulan(bulanPrefix)

    val totalPemasukan: Flow<Double> = dao.getTotalPemasukan()
    val totalPengeluaran: Flow<Double> = dao.getTotalPengeluaran()
    val totalSaldo: Flow<Double> = dao.getTotalSaldo()

    suspend fun insert(tx: CashTransactionEntity) = dao.insert(tx)

    suspend fun update(tx: CashTransactionEntity) = dao.insert(tx)

    suspend fun delete(tx: CashTransactionEntity) = dao.delete(tx)
}