package com.katar.app

import android.app.Application
import com.katar.app.data.KatarDatabase

/**
 * KatarApplication - Application class untuk inisialisasi global.
 *
 * Tanggung jawab:
 * - Inisialisasi Room Database (singleton)
 * - Menyediakan akses database ke seluruh aplikasi
 */
class KatarApplication : Application() {

    /** Database diinisialisasi lazy (baru dibuat saat pertama diakses). */
    val database: KatarDatabase by lazy {
        KatarDatabase.getInstance(this)
    }

    override fun onCreate() {
        super.onCreate()
        // Inisialisasi awal jika diperlukan (logging, crash reporter, dll.)
    }
}