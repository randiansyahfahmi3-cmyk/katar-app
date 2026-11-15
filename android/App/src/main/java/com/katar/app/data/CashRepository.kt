package com.katar.app.data

import kotlinx.coroutines.flow.Flow

class CashRepository(private val dao: CashTransactionDao) {
    val allTransactions: Flow<List<CashTransactionEntity>> = dao.getAll()
    val totalSaldo: Flow<Double?> = dao.getTotalSaldo()
    val totalPemasukan: Flow<Double?> = dao.getTotalPemasukan()
    val totalPengeluaran: Flow<Double?> = dao.getTotalPengeluaran()
    suspend fun insert(tx: CashTransactionEntity) = dao.insert(tx)
}