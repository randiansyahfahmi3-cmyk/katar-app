package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * MemberViewModel - Mengelola state UI untuk fitur Anggota.
 *
 * State yang dikelola:
 * - `members`: hasil akhir (sudah difilter + di-search) yang akan dirender UI
 * - `searchQuery`: query search dari user
 * - `filterActiveOnly`: filter aktif/semua
 * - `isLoading`: state loading (untuk UI nanti)
 * - `errorMessage`: pesan error (opsional)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MemberViewModel(private val repository: MemberRepository) : ViewModel() {

    // === State input dari UI ===
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterActiveOnly = MutableStateFlow(false)
    val filterActiveOnly: StateFlow<Boolean> = _filterActiveOnly.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // === Flow utama: hasil akhir ===
    // Reaktif: setiap kali searchQuery atau filterActiveOnly berubah,
    // hasilnya di-recompute dari repository
    val members: StateFlow<List<MemberEntity>> = combine(
        _searchQuery,
        _filterActiveOnly
    ) { query, activeOnly ->
        query to activeOnly
    }.flatMapLatest { (query, activeOnly) ->
        when {
            query.isBlank() && !activeOnly -> repository.getAllMembers()
            query.isBlank() && activeOnly -> repository.getActiveMembers()
            else -> repository.searchMembers(query)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // === Actions dari UI ===

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFilterActiveOnly() {
        _filterActiveOnly.value = !_filterActiveOnly.value
    }

    fun setFilterActiveOnly(active: Boolean) {
        _filterActiveOnly.value = active
    }

    fun addMember(member: MemberEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.insert(member)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menambah anggota: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateMember(member: MemberEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.update(member)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Gagal update anggota: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMember(member: MemberEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.delete(member)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Gagal hapus anggota: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}