package com.katar.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.katar.app.data.CashTransactionEntity
import com.katar.app.data.formatTanggalTampilan
import com.katar.app.data.toRupiah

/**
 * TransactionRow - Baris per transaksi kas.
 *
 * - Warna hijau + icon panah atas untuk PEMASUKAN
 * - Warna merah + icon panah bawah untuk PENGELUARAN
 */
@Composable
fun TransactionRow(
    transaction: CashTransactionEntity,
    modifier: Modifier = Modifier
) {
    val isPemasukan = transaction.tipe == "PEMASUKAN"
    val warna = if (isPemasukan) ColorHijau else ColorMerah
    val prefix = if (isPemasukan) "+" else "-"

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
            // Icon panah
            Surface(
                shape = CircleShape,
                color = warna.copy(alpha = 0.15f),
                modifier = Modifier.size(40.dp)
            ) {
                androidx.compose.foundation.layout.Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (isPemasukan) Icons.Filled.ArrowUpward
                                      else Icons.Filled.ArrowDownward,
                        contentDescription = transaction.tipe,
                        tint = warna,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info transaksi
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.judul,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${transaction.tanggal.formatTanggalTampilan()} • ${transaction.kategori}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Nominal
            Text(
                text = "$prefix${transaction.nominal.toRupiah()}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = warna
            )
        }
    }
}