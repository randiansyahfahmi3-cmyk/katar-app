package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KatarViewModelFactory(
    private val database: KatarDatabase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MemberViewModel::class.java) -> {
                MemberViewModel(MemberRepository(database.memberDao())) as T
            }

            modelClass.isAssignableFrom(CashViewModel::class.java) -> {
                CashViewModel(CashRepository(database.cashTransactionDao())) as T
            }

            modelClass.isAssignableFrom(EventViewModel::class.java) -> {
                EventViewModel(
                    EventRepository(
                        eventDao = database.eventDao(),
                        attendanceDao = database.attendanceDao(),
                        memberDao = database.memberDao()
                    )
                ) as T
            }

            else -> throw IllegalArgumentException(
                "Unknown ViewModel class: ${modelClass.name}"
            )
        }
    }
}