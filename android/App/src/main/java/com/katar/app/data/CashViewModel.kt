package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class CashViewModel(private val repository: CashRepository) : ViewModel() {

    private val _bulanFilter = MutableStateFlow<String?>(null)
    val bulanFilter: StateFlow<String?> = _bulanFilter.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val transactions: StateFlow<List<CashTransactionEntity>> = _bulanFilter
        .flatMapLatest { bulan ->
            if (bulan == null) repository.getAll()
            else repository.getByBulan(bulan)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val saldo: StateFlow<Double> = repository.totalSaldo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalPemasukan: StateFlow<Double> = repository.totalPemasukan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalPengeluaran: StateFlow<Double> = repository.totalPengeluaran
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun setBulanFilter(bulan: String?) {
        _bulanFilter.value = bulan
    }

    fun addTransaction(tx: CashTransactionEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.insert(tx)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Gagal tambah transaksi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTransaction(tx: CashTransactionEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.delete(tx)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Gagal hapus transaksi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

// ============================================================
// Utility functions - Helper untuk format tampilan
// ============================================================

/**
 * Format Double ke Rupiah: 1500000.0 → "Rp 1.500.000"
 */
fun Double.toRupiah(): String {
    val formatted = String.format(Locale.US, "%,.0f", this).replace(",", ".")
    return "Rp $formatted"
}

/**
 * Mendapatkan prefix bulan ini: "2026-11"
 * Digunakan untuk filter transaksi bulan berjalan.
 */
fun bulanIniPrefix(): String {
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH) + 1
    return String.format(Locale.US, "%04d-%02d", year, month)
}

/**
 * Format tanggal "2026-11-16" menjadi "16 Nov 2026"
 */
fun String.formatTanggalTampilan(): String {
    return try {
        val input = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val output = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val date = input.parse(this)
        if (date != null) output.format(date) else this
    } catch (e: Exception) {
        this
    }
}