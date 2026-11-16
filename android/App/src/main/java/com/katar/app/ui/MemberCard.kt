package com.katar.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katar.app.data.MemberEntity

/**
 * MemberCard - Card untuk menampilkan satu anggota.
 *
 * Menampilkan:
 * - Avatar inisial (warna berdasarkan jabatan)
 * - Nama + No Anggota
 * - Badge jabatan (warna berbeda per jabatan)
 * - RT/RW + No HP
 * - Tombol WhatsApp, Edit, Hapus
 */
@Composable
fun MemberCard(
    member: MemberEntity,
    onWhatsAppClick: (MemberEntity) -> Unit,
    onEditClick: (MemberEntity) -> Unit,
    onDeleteClick: (MemberEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // === Avatar inisial ===
            AvatarInisial(member = member)

            Spacer(modifier = Modifier.width(12.dp))

            // === Info anggota ===
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.nama,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (!member.no_anggota.isNullOrBlank()) {
                    Text(
                        text = "No: ${member.no_anggota}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.padding(top = 4.dp))

                // Badge jabatan
                if (!member.jabatan.isNullOrBlank()) {
                    JabatanBadge(jabatan = member.jabatan)
                }

                Spacer(modifier = Modifier.padding(top = 4.dp))

                // RT/RW + No HP
                val lokasi = buildString {
                    if (!member.rt.isNullOrBlank()) append("RT ${member.rt}")
                    if (!member.rw.isNullOrBlank()) {
                        if (isNotEmpty()) append(" / ")
                        append("RW ${member.rw}")
                    }
                }
                if (lokasi.isNotBlank()) {
                    Text(
                        text = "📍 $lokasi",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (!member.no_hp.isNullOrBlank()) {
                    Text(
                        text = "📞 ${member.no_hp}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // === Tombol aksi ===
            Row(horizontalArrangement = Arrangement.End) {
                if (!member.no_hp.isNullOrBlank()) {
                    IconButton(onClick = { onWhatsAppClick(member) }) {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            contentDescription = "Hubungi via WhatsApp",
                            tint = Color(0xFF25D366)  // hijau WhatsApp
                        )
                    }
                }
                IconButton(onClick = { onEditClick(member) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit anggota",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onDeleteClick(member) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Hapus anggota",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Avatar inisial: lingkaran berwarna dengan huruf awal nama.
 * Warna berubah berdasarkan jabatan.
 */
@Composable
private fun AvatarInisial(member: MemberEntity, modifier: Modifier = Modifier) {
    val inisial = member.nama
        .trim()
        .split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifBlank { "?" }

    val warna = warnaJabatan(member.jabatan)

    Surface(
        modifier = modifier.size(56.dp),
        shape = CircleShape,
        color = warna
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = inisial,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Badge jabatan dengan warna berdasarkan role.
 */
@Composable
private fun JabatanBadge(jabatan: String) {
    val warna = warnaJabatan(jabatan)
    Surface(
        shape = CircleShape,
        color = warna.copy(alpha = 0.15f),
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = jabatan,
            fontSize = 11.sp,
            color = warna,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

/**
 * Peta warna per jabatan.
 */
private fun warnaJabatan(jabatan: String?): Color {
    return when (jabatan?.lowercase()?.trim()) {
        "ketua" -> Color(0xFF1565C0)         // biru tua
        "wakil ketua", "wakil" -> Color(0xFF1976D2)  // biru medium
        "sekretaris" -> Color(0xFF2E7D32)    // hijau
        "bendahara" -> Color(0xFFEF6C00)     // orange
        else -> Color(0xFF616161)            // abu-abu (anggota)
    }
}