package com.katar.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.katar.app.data.CashTransactionEntity
import com.katar.app.data.bulanIniPrefix
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * CashFormDialog - Dialog untuk tambah transaksi kas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashFormDialog(
    onDismiss: () -> Unit,
    onSave: (CashTransactionEntity) -> Unit
) {
    var judul by remember { mutableStateOf("") }
    var nominalText by remember { mutableStateOf("") }
    var tipe by remember { mutableStateOf("PEMASUKAN") }
    var kategori by remember { mutableStateOf("Iuran") }
    var penanggungJawab by remember { mutableStateOf("") }
    var kategoriExpanded by remember { mutableStateOf(false) }

    var judulError by remember { mutableStateOf<String?>(null) }
    var nominalError by remember { mutableStateOf<String?>(null) }

    val daftarKategori = listOf("Iuran", "Donasi", "Operasional", "Lainnya")

    val tanggalHariIni: String = remember {
        val cal = Calendar.getInstance()
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
    }

    fun validate(): Boolean {
        judulError = null
        nominalError = null
        if (judul.isBlank()) {
            judulError = "Judul wajib diisi"
            return false
        }
        val nominal = nominalText.toDoubleOrNull()
        if (nominal == null || nominal <= 0) {
            nominalError = "Nominal harus lebih dari 0"
            return false
        }
        return true
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Transaksi") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Judul
                OutlinedTextField(
                    value = judul,
                    onValueChange = {
                        judul = it
                        if (judulError != null) judulError = null
                    },
                    label = { Text("Judul *") },
                    isError = judulError != null,
                    supportingText = judulError?.let { { Text(it) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Nominal
                OutlinedTextField(
                    value = nominalText,
                    onValueChange = {
                        nominalText = it.filter { c -> c.isDigit() }
                        if (nominalError != null) nominalError = null
                    },
                    label = { Text("Nominal (Rp) *") },
                    isError = nominalError != null,
                    supportingText = nominalError?.let { { Text(it) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Tipe (PEMASUKAN/PENGELUARAN)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = tipe == "PEMASUKAN",
                        onClick = { tipe = "PEMASUKAN" },
                        label = { Text("Pemasukan") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = tipe == "PENGELUARAN",
                        onClick = { tipe = "PENGELUARAN" },
                        label = { Text("Pengeluaran") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Kategori (dropdown)
                ExposedDropdownMenuBox(
                    expanded = kategoriExpanded,
                    onExpandedChange = { kategoriExpanded = !kategoriExpanded }
                ) {
                    OutlinedTextField(
                        value = kategori,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategori") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = kategoriExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = kategoriExpanded,
                        onDismissRequest = { kategoriExpanded = false }
                    ) {
                        daftarKategori.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    kategori = option
                                    kategoriExpanded = false
                                }
                            )
                        }
                    }
                }

                // Penanggung Jawab
                OutlinedTextField(
                    value = penanggungJawab,
                    onValueChange = { penanggungJawab = it },
                    label = { Text("Penanggung Jawab") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (validate()) {
                        val nominal = nominalText.toDoubleOrNull() ?: 0.0
                        val entity = CashTransactionEntity(
                            id = 0,
                            judul = judul.trim(),
                            nominal = nominal,
                            tipe = tipe,
                            kategori = kategori,
                            tanggal = tanggalHariIni,
                            penanggung_jawab = penanggungJawab.trim().ifBlank { null }
                        )
                        onSave(entity)
                    }
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}