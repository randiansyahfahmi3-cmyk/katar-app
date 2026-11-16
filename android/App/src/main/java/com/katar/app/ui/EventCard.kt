package com.katar.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katar.app.data.EventEntity
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * EventCard - Card per kegiatan.
 *
 * Menampilkan:
 * - Badge status (AKAN_DATANG hijau / SELESAI abu-abu)
 * - Nama kegiatan (bold)
 * - Tanggal & waktu
 * - Lokasi
 * - Tombol Absensi, Edit, Hapus
 */
@Composable
fun EventCard(
    event: EventEntity,
    onAbsensiClick: (EventEntity) -> Unit,
    onEditClick: (EventEntity) -> Unit,
    onDeleteClick: (EventEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAkanDatang = event.status == "AKAN_DATANG"
    val statusColor = if (isAkanDatang) Color(0xFF2E7D32) else Color(0xFF616161)
    val statusText = if (isAkanDatang) "AKAN DATANG" else "SELESAI"

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // === Baris atas: badge status + tanggal ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = statusText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = formatTanggal(event.tanggal_waktu),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            // === Nama kegiatan ===
            Text(
                text = event.nama_kegiatan,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // === Deskripsi (opsional) ===
            if (!event.deskripsi.isNullOrBlank()) {
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = event.deskripsi,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // === Lokasi ===
            if (!event.lokasi.isNullOrBlank()) {
                Spacer(modifier = Modifier.padding(top = 6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = "Lokasi",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.lokasi,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // === Tombol aksi ===
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { onAbsensiClick(event) }) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Absensi",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Absensi", fontSize = 12.sp)
                }
                IconButton(onClick = { onEditClick(event) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = { onDeleteClick(event) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Hapus",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Format tanggal dari "yyyy-MM-dd HH:mm" ke "dd MMM yyyy, HH:mm".
 */
private fun formatTanggal(input: String): String {
    return try {
        val inFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        val outFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        val date = inFormat.parse(input)
        if (date != null) outFormat.format(date) else input
    } catch (e: Exception) {
        input
    }
}