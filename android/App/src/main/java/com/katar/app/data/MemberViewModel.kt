package com.katar.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katar.app.data.MemberEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flowMutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * MemberViewModel - ViewModel layer for Member management.
 * Memperoleh data dari Repository dan menyediakan state untuk UI.
 */
class MemberViewModel(private val repository: MemberRepository) : ViewModel() {

    /** All members observable Flow */
    private val _allMembers = repository.allMembers
    val allMembers: Flow<List<MemberEntity>> = _allMembers

    /** Active members observable Flow */
    private val _activeMembers = repository.activeMembers
    val activeMembers: Flow<List<MemberEntity>> = _activeMembers

    /** Current search query state */
    var searchQuery by mutableStateOf("") // Compose compatible
    var selectedFilter by mutableStateOf("all") // "all", "active", "search"

    /** Filtered members based on current query and filter */
    val filteredMembers: Flow<List<MemberEntity>> = combine(
        _allMembers,
        _activeMembers
    ) { all, active ->
        when (selectedFilter) {
            "active" -> active.filter { it.status_aktif }
            "search" -> {
                // Apply search logic later in UI or here
                all
            }
            else -> all
        }
    }.map { list ->
        if (searchQuery.isNotEmpty()) {
            list.filter {
                it.nama.toLowerCase().contains(searchQuery.toLowerCase()) ||
                    (it.jabatan ?: "").toLowerCase().contains(searchQuery.toLowerCase())
            }
        } else {
            list
        }
    }

    /** Toggle between all/active filter */
    fun setFilter(filter: String) {
        selectedFilter = filter
    }

    /** Set search query */
    fun setSearchQuery(query: String) {
        searchQuery = query
    }

    /** Clear search and reset to all members */
    fun clearSearch() {
        searchQuery = ""
        selectedFilter = "all"
    }

    /** Insert a new member */
    fun insertMember(member: MemberEntity) = viewModelScope.launch {
        repository.insertMember(member)
    }

    /** Update an existing member */
    fun updateMember(member: MemberEntity) = viewModelScope.launch {
        repository.updateMember(member)
    }

    /** Delete all members */
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    /** Check if member with no_anggota exists */
    fun isMemberExists(no_anggota: String): Boolean = repository.isMemberExists(no_anggota)
}