package com.katar.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katar.app.data.CashViewModel
import com.katar.app.data.bulanIniPrefix

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashScreen(viewModel: CashViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    val saldo by viewModel.saldo.collectAsState()
    val pemasukan by viewModel.totalPemasukan.collectAsState()
    val pengeluaran by viewModel.totalPengeluaran.collectAsState()
    val bulanFilter by viewModel.bulanFilter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    val bulanIni = remember { bulanIniPrefix() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Kas Karang Taruna",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "${transactions.size} transaksi",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Transaksi")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SaldoCard(saldo = saldo)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryCard(
                        label = "Pemasukan",
                        value = pemasukan,
                        accentColor = ColorHijau,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        label = "Pengeluaran",
                        value = pengeluaran,
                        accentColor = ColorMerah,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    }
                }
            }

            if (errorMessage != null) {
                item {
                    Text(
                        text = "⚠️ $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Riwayat Transaksi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilterChip(
                            selected = bulanFilter == null,
                            onClick = { viewModel.setBulanFilter(null) },
                            label = { Text("Semua", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = bulanFilter == bulanIni,
                            onClick = {
                                viewModel.setBulanFilter(
                                    if (bulanFilter == bulanIni) null else bulanIni
                                )
                            },
                            label = { Text("Bulan Ini", fontSize = 11.sp) }
                        )
                    }
                }
            }

            if (transactions.isEmpty()) {
                item {
                    EmptyStateKas(
                        isFiltered = bulanFilter != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
            } else {
                items(transactions, key = { it.id }) { tx ->
                    TransactionRow(transaction = tx)
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showAddDialog) {
        CashFormDialog(
            onDismiss = { showAddDialog = false },
            onSave = {
                viewModel.addTransaction(it)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun EmptyStateKas(
    isFiltered: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (isFiltered) "📅" else "💰",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isFiltered) "Tidak ada transaksi bulan ini"
                       else "Belum ada transaksi",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isFiltered) "Klik 'Semua' untuk lihat semua"
                       else "Klik tombol + untuk menambah transaksi",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}