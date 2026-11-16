package com.katar.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * KatarDatabase - Room Database utama aplikasi KATAR.
 *
 * Catatan penting:
 * - Tidak butuh @TypeConverters karena semua field tanggal/enum
 *   sudah disimpan sebagai String.
 * - fallbackToDestructiveMigration() dipakai untuk tahap development
 *   (database dihapus & dibuat ulang jika schema berubah).
 *   JANGAN pakai ini di production!
 */
@Database(
    entities = [
        MemberEntity::class,
        CashTransactionEntity::class,
        EventEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KatarDatabase : RoomDatabase() {

    // === DAO Accessors ===
    abstract fun memberDao(): MemberDao
    abstract fun cashTransactionDao(): CashTransactionDao
    abstract fun eventDao(): EventDao

    companion object {
        private const val DATABASE_NAME = "katar.db"

        @Volatile
        private var INSTANCE: KatarDatabase? = null

        /**
         * Mendapatkan instance database (singleton).
         * Pattern double-checked locking agar thread-safe.
         */
        fun getInstance(context: Context): KatarDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): KatarDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                KatarDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()  // Untuk development
                .build()
        }
    }
}