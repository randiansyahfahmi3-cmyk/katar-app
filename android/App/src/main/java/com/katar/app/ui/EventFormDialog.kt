package com.katar.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.katar.app.data.EventEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * EventFormDialog - Dialog untuk tambah/edit kegiatan.
 *
 * @param event Jika null → mode tambah. Jika ada → mode edit.
 */
@Composable
fun EventFormDialog(
    event: EventEntity? = null,
    onDismiss: () -> Unit,
    onSave: (EventEntity) -> Unit
) {
    val isEditMode = event != null

    // === Form state ===
    var nama by remember { mutableStateOf(event?.nama_kegiatan ?: "") }
    var deskripsi by remember { mutableStateOf(event?.deskripsi ?: "") }
    var lokasi by remember { mutableStateOf(event?.lokasi ?: "") }
    var tanggalWaktu by remember {
        mutableStateOf(event?.tanggal_waktu ?: defaultTanggal())
    }
    var status by remember { mutableStateOf(event?.status ?: "AKAN_DATANG") }

    var namaError by remember { mutableStateOf<String?>(null) }
    var tanggalError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        namaError = null
        tanggalError = null

        if (nama.isBlank()) {
            namaError = "Nama kegiatan wajib diisi"
            return false
        }
        if (tanggalWaktu.isBlank()) {
            tanggalError = "Tanggal wajib diisi"
            return false
        }

        // Validasi format tanggal sederhana
        val regex = Regex("""\d{4}-\d{2}-\d{2} \d{2}:\d{2}""")
        if (!regex.matches(tanggalWaktu)) {
            tanggalError = "Format: yyyy-MM-dd HH:mm (contoh: 2026-11-25 19:00)"
            return false
        }

        return true
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditMode) "Edit Kegiatan" else "Tambah Kegiatan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Nama
                OutlinedTextField(
                    value = nama,
                    onValueChange = {
                        nama = it
                        if (namaError != null) namaError = null
                    },
                    label = { Text("Nama Kegiatan *") },
                    isError = namaError != null,
                    supportingText = namaError?.let { { Text(it) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Deskripsi
                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Deskripsi") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                // Lokasi
                OutlinedTextField(
                    value = lokasi,
                    onValueChange = { lokasi = it },
                    label = { Text("Lokasi") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Tanggal & Waktu
                OutlinedTextField(
                    value = tanggalWaktu,
                    onValueChange = {
                        tanggalWaktu = it
                        if (tanggalError != null) tanggalError = null
                    },
                    label = { Text("Tanggal & Waktu *") },
                    isError = tanggalError != null,
                    supportingText = {
                        Text(tanggalError ?: "Format: yyyy-MM-dd HH:mm")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Status chips
                Text("Status", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = status == "AKAN_DATANG",
                        onClick = { status = "AKAN_DATANG" },
                        label = { Text("Akan Datang") }
                    )
                    FilterChip(
                        selected = status == "SELESAI",
                        onClick = { status = "SELESAI" },
                        label = { Text("Selesai") }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (validate()) {
                        val result = EventEntity(
                            id = event?.id ?: 0,
                            nama_kegiatan = nama.trim(),
                            deskripsi = deskripsi.trim().ifBlank { null },
                            lokasi = lokasi.trim().ifBlank { null },
                            tanggal_waktu = tanggalWaktu.trim(),
                            status = status
                        )
                        onSave(result)
                    }
                }
            ) {
                Text(if (isEditMode) "Simpan Perubahan" else "Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

/**
 * Generate tanggal default: 7 hari dari sekarang, jam 19:00.
 */
private fun defaultTanggal(): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, 7)
    cal.set(Calendar.HOUR_OF_DAY, 19)
    cal.set(Calendar.MINUTE, 0)
    return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(cal.time)
}