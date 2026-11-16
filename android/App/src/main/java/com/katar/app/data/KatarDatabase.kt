package com.katar.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        MemberEntity::class,
        CashTransactionEntity::class,
        EventEntity::class,
        AttendanceEntity::class   // ← TAMBAHKAN INI
    ],
    version = 2,                  // ← NAIKKAN VERSI (dari 1 ke 2)
    exportSchema = false
)
abstract class KatarDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun cashTransactionDao(): CashTransactionDao
    abstract fun eventDao(): EventDao
    abstract fun attendanceDao(): AttendanceDao   // ← TAMBAHKAN INI

    companion object {
        private const val DATABASE_NAME = "katar.db"

        @Volatile
        private var INSTANCE: KatarDatabase? = null

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
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}