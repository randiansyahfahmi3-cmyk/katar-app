package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flowMutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.Locale

/**
 * CashViewModel - ViewModel layer for Cash Transaction management.
 * Menyediakan state terstruktur untuk CashScreen dengan perhitungan saldo real-time.
 */
class CashViewModel(private val repository: CashRepository) : ViewModel() {

    /** Saldo total reaktif */
    private val _totalSaldo = repository.totalSaldoFlow
        .stateIn(
            scope = viewModelScope,
            initialValue = 0.0,
            operator = { it }
        )
    val totalSaldo: Flow<Double> = _totalSaldo

    /** Total pemasukan reaktif */
    private val _totalPemasukan = repository.totalPemasukanFlow
        .stateIn(
            scope = viewModelScope,
            initialValue = 0.0,
            operator = { it }
        )
    val totalPemasukan: Flow<Double> = _totalPemasukan

    /** Total pengeluaran reaktif */
    private val _totalPengeluaran = repository.totalPengeluaranFlow
        .stateIn(
            scope = viewModelScope,
            initialValue = 0.0,
            operator = { it }
        )
    val totalPengeluaran: Flow<Double> = _totalPengeluaran

    /** Format uang ke format Rupiah */
    private val formatRupiah = DecimalFormat("#,##0")

    /** Format uang ke Rupiah string */
    fun formatUang(nominal: Double): String {
        return "Rp ${formatRupiah.format(nominal)}"
    }

    /** Hitung saldo display */
    val saldoDisplay: String
        get() = formatUang(totalSaldo.value())

    /** Hitung pemasukan display */
    val pemasukanDisplay: String
        get() = formatUang(totalPemasukan.value())

    /** Hitung pengeluaran display */
    val pengeluaranDisplay: String
        get() = formatUang(totalPengeluaran.value())

    /** Status warna saldo */
    val saldoWarna: String
        get() = when (totalSaldo.value() >= 0) {
            true -> "hijau" // Saldo positif
            else -> "merah" // Saldo negatif
        }

    /** Insert transaksi baru */
    fun insertTransaction(transaction: CashTransactionEntity) = viewModelScope.launch {
        repository.insertTransaction(transaction)
    }

    /** Update transaksi */
    fun updateTransaction(transaction: CashTransactionEntity) = viewModelScope.launch {
        repository.updateTransaction(transaction)
    }

    /** Hapus semua transaksi */
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}