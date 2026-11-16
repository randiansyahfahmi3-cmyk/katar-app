package com.katar.app.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * WhatsAppHelper - Utility untuk membuka WhatsApp dengan nomor tertentu.
 *
 * Normalisasi nomor:
 * - "081234567890" → "6281234567890"
 * - "+6281234567890" → "6281234567890"
 * - "6281234567890" → "6281234567890"
 *
 * Kalau WhatsApp tidak terinstall, tampilkan Toast error.
 */
fun openWhatsApp(context: Context, phone: String) {
    if (phone.isBlank()) {
        Toast.makeText(context, "Nomor HP kosong", Toast.LENGTH_SHORT).show()
        return
    }

    val normalized = phone
        .replace(Regex("[^0-9]"), "")
        .let { if (it.startsWith("0")) "62${it.substring(1)}" else it }

    if (normalized.length < 10) {
        Toast.makeText(context, "Nomor HP tidak valid", Toast.LENGTH_SHORT).show()
        return
    }

    val url = "https://wa.me/$normalized"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp tidak terinstall", Toast.LENGTH_SHORT).show()
    }
}