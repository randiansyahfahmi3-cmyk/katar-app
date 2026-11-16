package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * KatarViewModelFactory - Pabrik ViewModel dengan dependency injection manual.
 *
 * Tujuan:
 * - Menyediakan Repository ke setiap ViewModel saat dibuat.
 * - Menghindari ViewModel default (yang tidak bisa di-inject Repository).
 */
class KatarViewModelFactory(
    private val database: KatarDatabase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MemberViewModel::class.java) -> {
                val repository = MemberRepository(database.memberDao())
                MemberViewModel(repository) as T
            }

            modelClass.isAssignableFrom(CashViewModel::class.java) -> {
                val repository = CashRepository(database.cashTransactionDao())
                CashViewModel(repository) as T
            }

            modelClass.isAssignableFrom(EventViewModel::class.java) -> {
                val repository = EventRepository(database.eventDao())
                EventViewModel(repository) as T
            }

            else -> throw IllegalArgumentException(
                "Unknown ViewModel class: ${modelClass.name}"
            )
        }
    }
}