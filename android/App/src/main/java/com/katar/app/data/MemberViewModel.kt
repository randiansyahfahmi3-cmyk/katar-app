package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MemberViewModel(private val repo: MemberRepository) : ViewModel() {
    val members = repo.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun add(member: MemberEntity) = viewModelScope.launch { repo.insert(member) }
    fun delete(member: MemberEntity) = viewModelScope.launch { repo.delete(member) }
}