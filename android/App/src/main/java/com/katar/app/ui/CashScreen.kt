package com.katar.app.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement.Align
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxIntrinsics
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.DpArrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.Chip
import androidx.compose.material3.ChipGroup
import androidx.compose.material3.Dialog
import androidx.compose.material3.DialogPane
import androidx.compose.material3.Dismiss
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ImageVector
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldValue
import androidx.compose.material3.TextInputLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Disposal
import androidx.compose.runtime.MutableStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.sideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.textUnit
import androidx.compose.ui.unit.size
import androidx.compose.ui.unit.width
import com.katar.app.R
import com.katar.app.data.CashTransactionEntity
import com.katar.app.data.CashViewModel
import com.katar.app.data.MemberEntity
import com.katar.app.navigation.route
import com.katar.app.navigation.routeString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.run.catcher

/**
 * CashScreen - Compose UI for Cash/Kas Karang Taruna management.
 * Menampilkan kartu ringkasan saldo di bagian atas, riwayat transaksi
 * dengan indikator warna, dan dialog tambah transaksi baru.
 */
@Composable
fun CashScreen(viewModel: CashViewModel = CashViewModel(CashRepository(CashTransactionDao())) ) {

    var showAddDialog by remember { mutableStateOf(false) }
    var judul by remember { mutableStateOf("") }
    var nominal by remember { mutableStateOf("0") }
    var kategori by remember { mutableStateOf("Iuran") }
    var selectedTipe by remember { mutableStateOf("PEMASUKAN") }

    // Collect summary data
    var totalSaldo by remember { mutableStateOf<String>("Rp 0") }
    var totalPemasukan by remember { mutableStateOf<String>("Rp 0") }
    var totalPengeluaran by remember { mutableStateOf<String>("Rp 0") }

    viewModel.totalSaldo.collect { saldo ->
        totalSaldo = saldo
    }
    viewModel.totalPemasukan.collect { pemasukan ->
        totalPemasukan = pemasukan
    }
    viewModel.totalPengeluaran.collect { pengeluaran ->
        totalPengeluaran = pengeluaran
    }

    // Collect transactions for history
    var transaksiList by remember { mutableStateOf<List<CashTransactionEntity>>() }
    viewModel.when {
        // Note: In real implementation, would collect from repository
    }

    // For now, use empty list or mock data
    // In full implementation: viewModel.pemasukanFlow.collect { ... }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = DpArrangement.SpaceBetween
    ) {
        // --- SUMMARY CARDS SECTION ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(MaterialTheme.colorScheme.surface),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = androidx.compose.ui.Alignment.Start
            ) {
                // Saldo Card - atur warna berdasarkan nilai
                SaldoCard(
                    saldo = totalSaldo,
                    warna = when (totalSaldo.valueAsNumber) {
                        null -> "abbu"
                        else -> if (totalSaldo.contains("Rp -")) "merah" else "hijau"
                    }
                )

                // Pemasukan Card
                SummaryCard(
                    label = "Pemasukan Bulan Ini",
                    value = totalPemasukan,
                    icon = "receipt",
                    warna = MaterialTheme.colorScheme.tertiary,
                    warnaTeks = MaterialTheme.colorScheme.onTertiary
                )

                // Pengeluaran Card
                SummaryCard(
                    label = "Pengeluaran Bulan Ini",
                    value = totalPengeluaran,
                    icon = "delete",
                    warna = MaterialTheme.colorScheme.error,
                    warnaTeks = MaterialTheme.colorScheme.onError
                )
            }
        }

        // --- TRANSACTION HISTORY SECTION ---
        Text(
            text = "Riwayat Transaksi",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // List transaksi - menggunakan LazyColumn concept, sementara Column
        if (transaksiList.isEmpty()) {
            Text(
                text = "Belum ada transaksi terdaftar",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            // Mock: ambil transaksi dari viewModel
            // Di implementasi full: collect dari repository.pemasukanFlow + pengeluaranFlow
            transaksiList.forEach { transaction ->
                TransactionRow(transaction = transaction)
                    .modifier(Modifier.padding(bottom = 8.dp))
            }
        }

        // --- ADD TRANSACTION BOTTOM AREA ---
        OutlinedTextField(
            value = nominal,
            onValueChange = { nominal = it },
            placeholder = { Text(text = "Masukkan nominal") },
            modifier = Modifier.fillMaxWidth(0.8f).padding(4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            // Pilih tipe PEMASUKAN/PENGELUARAN
            ChipGroup(
                value = selectedTipe,
                onValueChanged = { selectedTipe = it },
                arrangement = Arrangement.SpaceBetween
            ) {
                Chip(
                    text = { Text(text = "Masuk") },
                    selected = selectedTipe == "PEMASUKAN",
                    onClick = { selectedTipe = "PEMASUKAN" }
                )
                Chip(
                    text = { Text(text = "Keluar") },
                    selected = selectedTipe == "PENGELUARAN",
                    onClick = { selectedTipe = "PENGELUARAN" }
                )
            }

            // Tombol Tambah
            OutlinedButton(
                onClick = {
                    // Validasi dan tambah transaksi
                    val nominalDouble = nominal.replace("Rp ", "").replace(".", "").toDoubleOrNull() ?: 0
                    val transaction = CashTransactionEntity(
                        id = 0,
                        judul = judul,
                        nominal = nominalDouble,
                        tipe = selectedTipe,
                        kategori = kategori,
                        tanggal = java.time.LocalDate.now().toString(),
                        penanggung_jawab = "Ketua KATAR"
                    )
                    viewModel.insertTransaction(transaction)

                    // Reset form
                    judul = ""
                    nominal = "0"
                    selectedTipe = "PEMASUKAN"
                    kategori = "Iuran"
                    showAddDialog = false
                }
            ) {
                Text(text = "Tambah Transaksi")
            }
        }
    }
}

/** SaldoCard - Card menampilkan total saldo dengan warna indikator */
@Composable
fun SaldoCard(
    saldo: String,
    warna: String = "hijau"
) {
    val warnaBg = when (warna) {
        "hijau" -> MaterialTheme.colorScheme.tertiaryContainer
        "merah" -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surface
    }
    val warnaTeks = when (warna) {
        "hijau" -> MaterialTheme.colorScheme.onTertiaryContainer
        "merah" -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(warnaBg)
            .clip(shape = androidx.compose.ui.shape.RoundedCornerShape(16.dp)),
        elevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .size(BoxIntrinsics.Min)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = saldo,
                    style = MaterialTheme.typography.h5,
                    color = warnaTeks
                )
                Text(
                    text = "Saldo Kas KATAR",
                    style = MaterialTheme.typography.caption,
                    color = warnaTeks.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/** SummaryCard - Card ringkasan sederhana */
@Composable
fun SummaryCard(
    label: String,
    value: String,
    icon: String,
    warna: Color,
    warnaTeks: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon placeholder
            Icon(
                imageVector = if (icon == "receipt") {
                    androidx.compose.material3.Icons.Filled.Receipt
                } else {
                    androidx.compose.material3.Icons.Filled.Delete
                },
                contentDescription = icon,
                tint = warna
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxWeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = warnaTeks.copy(alpha = 0.7f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.body1,
                    color = warnaTeks,
                    overflow = androidx.compose.ui.textOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(8.dp))

            // Indicator badge
            Chip(
                text = { Text(text = "Rp") },
                backgroundColor = warna,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

/** TransactionRow - Baris individu transaksi dengan warna indikator */
@Composable
fun TransactionRow(transaction: CashTransactionEntity) {
    val isMasuk = transaction.tipe == "PEMASUKAN"
    val warnaIndikator = if (isMasuk) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.error
    }
    val warnaTeks = if (isMasuk) {
        MaterialTheme.colorScheme.onTertiary
    } else {
        MaterialTheme.colorScheme.onError
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(if (isMasuk) {
                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
            }),
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tanggal
            Text(
                text = transaction.tanggal,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                width = 100.dp
            )

            // Judul
            Text(
                text = transaction.judul,
                style = MaterialTheme.typography.body2,
                overflow = androidx.compose.ui.textOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.width(12.dp))

            // Nominal dengan warna
            Text(
                text = viewModel?.formatUang(transaction.nominal) ?: "Rp 0",
                style = MaterialTheme.typography.body2,
                color = warnaIndikator
            )

            Spacer(Modifier.width(12.dp))

            // Badge tipe
            Chip(
                text = { Text(text = transaction.tipe, overflow = androidx.compose.ui.textOverflow.Ellipsis) },
                backgroundColor = warnaIndikator,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}