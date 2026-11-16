package com.katar.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.katar.app.data.MemberEntity

/**
 * MemberFormDialog - Dialog untuk tambah/edit anggota.
 *
 * Mode:
 * - Add: member = null → form kosong
 * - Edit: member = existing → form terisi
 *
 * Validasi:
 * - Nama wajib diisi
 * - No HP optional, tapi kalau diisi minimal 10 digit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberFormDialog(
    member: MemberEntity? = null,
    onDismiss: () -> Unit,
    onSave: (MemberEntity) -> Unit
) {
    val isEditMode = member != null

    // === Form state ===
    var nama by remember { mutableStateOf(member?.nama ?: "") }
    var noAnggota by remember { mutableStateOf(member?.no_anggota ?: "") }
    var jabatan by remember { mutableStateOf(member?.jabatan ?: "Anggota") }
    var rt by remember { mutableStateOf(member?.rt ?: "") }
    var rw by remember { mutableStateOf(member?.rw ?: "") }
    var noHp by remember { mutableStateOf(member?.no_hp ?: "") }
    var statusAktif by remember { mutableStateOf(member?.status_aktif ?: true) }

    // === Error state ===
    var namaError by remember { mutableStateOf<String?>(null) }
    var noHpError by remember { mutableStateOf<String?>(null) }

    // === Dropdown state ===
    var jabatanExpanded by remember { mutableStateOf(false) }
    val daftarJabatan = listOf("Ketua", "Wakil Ketua", "Sekretaris", "Bendahara", "Anggota")

    fun validate(): Boolean {
        namaError = null
        noHpError = null

        if (nama.isBlank()) {
            namaError = "Nama wajib diisi"
            return false
        }
        if (noHp.isNotBlank() && noHp.replace(Regex("[^0-9]"), "").length < 10) {
            noHpError = "No HP minimal 10 digit"
            return false
        }
        return true
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEditMode) "Edit Anggota" else "Tambah Anggota Baru")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Nama (wajib)
                OutlinedTextField(
                    value = nama,
                    onValueChange = {
                        nama = it
                        if (namaError != null) namaError = null
                    },
                    label = { Text("Nama *") },
                    isError = namaError != null,
                    supportingText = namaError?.let { { Text(it) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // No Anggota
                OutlinedTextField(
                    value = noAnggota,
                    onValueChange = { noAnggota = it },
                    label = { Text("No Anggota") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Jabatan (dropdown)
                ExposedDropdownMenuBox(
                    expanded = jabatanExpanded,
                    onExpandedChange = { jabatanExpanded = !jabatanExpanded }
                ) {
                    OutlinedTextField(
                        value = jabatan,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Jabatan") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = jabatanExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = jabatanExpanded,
                        onDismissRequest = { jabatanExpanded = false }
                    ) {
                        daftarJabatan.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    jabatan = option
                                    jabatanExpanded = false
                                }
                            )
                        }
                    }
                }

                // RT & RW dalam satu baris
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = rt,
                        onValueChange = { rt = it.filter { c -> c.isDigit() } },
                        label = { Text("RT") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = rw,
                        onValueChange = { rw = it.filter { c -> c.isDigit() } },
                        label = { Text("RW") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                // No HP
                OutlinedTextField(
                    value = noHp,
                    onValueChange = {
                        noHp = it.filter { c -> c.isDigit() || c == '+' || c == '-' }
                        if (noHpError != null) noHpError = null
                    },
                    label = { Text("No HP") },
                    isError = noHpError != null,
                    supportingText = noHpError?.let { { Text(it) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                // Status aktif
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Status Aktif")
                    Switch(
                        checked = statusAktif,
                        onCheckedChange = { statusAktif = it }
                    )
                }

                Spacer(modifier = Modifier.padding(top = 4.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (validate()) {
                        val result = MemberEntity(
                            id = member?.id ?: 0,
                            nama = nama.trim(),
                            no_anggota = noAnggota.trim().ifBlank { null },
                            jabatan = jabatan.trim().ifBlank { null },
                            rt = rt.trim().ifBlank { null },
                            rw = rw.trim().ifBlank { null },
                            no_hp = noHp.trim().ifBlank { null },
                            status_aktif = statusAktif
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