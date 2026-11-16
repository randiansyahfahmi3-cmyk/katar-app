package com.katar.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katar.app.data.toRupiah

/**
 * SummaryCard - Card kecil untuk ringkasan pemasukan/pengeluaran.
 *
 * @param label Label seperti "Pemasukan" atau "Pengeluaran"
 * @param value Nominal
 * @param accentColor Warna aksen (hijau untuk masuk, merah untuk keluar)
 */
@Composable
fun SummaryCard(
    label: String,
    value: Double,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = accentColor.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value.toRupiah(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }
    }
}

// Warna helper
val ColorHijau: Color = Color(0xFF2E7D32)
val ColorMerah: Color = Color(0xFFC62828)