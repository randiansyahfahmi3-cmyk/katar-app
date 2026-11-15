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
 * Kas Screen - Placeholder
 * Displayed for cash and financial reporting.
 */
@Composable
fun KasScreen() {
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
                text = "Kas & Laporan Keuangan",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Layar untuk tracking kas dan laporan keuangan",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Fitur yang akan tersedia:\n• Pemasukan & Pengeluaran\n• Kategori Pengeluaran\n• Laporan Bulanan\n• Tracking Balance",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = androidx.compose.ui.Alignment.TextAlignment.Center
            )
        }
    }
}