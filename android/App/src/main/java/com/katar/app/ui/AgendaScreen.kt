package com.katar.app.ui

import androidx.compose.foundation.layout.Arrangement.center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.katar.app.R

/**
 * Agenda Screen - Placeholder
 * Displayed for agenda kegiatan and absensi rapat.
 */
@Composable
fun AgendaScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center(),
            horizontalAlignment = androidx.compose.ui.Alignment.Horizontal.Center
        ) {
            Text(
                text = "Agenda Kegiatan & Absensi",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Layar untuk agenda kegiatan dan absensi rapat",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Fitur yang akan tersedia:\n• Daftar Agenda Kegiatan\n• Form Absensi Rapat\n• Status Kehadiran\n• Riwayat Absensi",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = androidx.compose.ui.Alignment.TextAlignment.Center
            )
        }
    }
}