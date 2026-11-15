package com.katar.app.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow MutableFlow
import kotlinx.coroutines.flow.flow

/**
 * MemberRepository - Repository layer for Member data source.
 * Mengimplementasikan aturan bisnis untuk akses data anggota.
 */
class MemberRepository(private val memberDao: MemberDao) {

    /** Get all members as Flow */
    val allMembers: Flow<List<MemberEntity>> = memberDao.getAllMembers()

    /** Get only active members */
    val activeMembers: Flow<List<MemberEntity>> = memberDao.getActiveMembers()

    /** Search members by name or position */
    suspend fun searchByNameOrJabatan(nama: String, jabatan: String): List<MemberEntity> {
        return memberDao.searchByNameOrJabatan(nama, jabatan)
    }

    /** Search active members by name or position */
    suspend fun searchActiveByNameOrJabatan(nama: String, jabatan: String): List<MemberEntity> {
        return memberDao.searchActiveByNameOrJabatan(nama, jabatan)
    }

    /** Get count of all members */
    suspend fun getMemberCount(): Int = memberDao.getAllMembers().awaitFirst().size

    /** Get count of active members */
    suspend fun getActiveMemberCount(): Int = memberDao.getActiveMembers().awaitFirst().size

    /** Check if member exists by NIK/no_anggota */
    suspend fun isMemberExists(no_anggota: String): Boolean {
        return memberDao.getAllMembers()
            .awaitFirst()
            .any { it.no_anggota == no_anggota }
    }

    /** Insert member with suspend function */
    suspend fun insertMember(member: MemberEntity) {
        memberDao.insertMember(member)
    }

    /** Update member with suspend function */
    suspend fun updateMember(member: MemberEntity) {
        memberDao.updateMember(member)
    }

    /** Delete all members */
    suspend fun deleteAll() {
        memberDao.deleteAllMembers()
    }
}