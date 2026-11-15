package com.katar.app.ui

import androidx.compose.foundation.layout.Arrangement.center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.katar.app.R

/**
 * Beranda (Home) Screen - Placeholder
 * Displayed as the first destination in the Bottom Navigation Bar.
 */
@Composable
fun BerandaScreen() {
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
                text = "Beranda",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Selamat Datang di Aplikasi KATAR",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Beranda merupakan layar utama aplikasi.\n\nFitur yang akan segera tersedia:\n• Agenda Kegiatan Terbaru\n• Pengumuman Terbaru\n• Ringkas Kas Keuangan",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = androidx.compose.ui.Alignment.TextAlignment.Center
            )
        }
    }
}