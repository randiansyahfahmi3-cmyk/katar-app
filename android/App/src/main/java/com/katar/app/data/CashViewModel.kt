package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CashViewModel(private val repo: CashRepository) : ViewModel() {
    val transactions = repo.allTransactions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val saldo = repo.totalSaldo.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    val pemasukan = repo.totalPemasukan.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    val pengeluaran = repo.totalPengeluaran.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    fun add(tx: CashTransactionEntity) = viewModelScope.launch { repo.insert(tx) }
}