package com.katar.app.data

import kotlinx.coroutines.flow.Flow

class MemberRepository(private val dao: MemberDao) {

    fun getAllMembers(): Flow<List<MemberEntity>> = dao.getAll()

    suspend fun insert(member: MemberEntity) {
        dao.insert(member)
    }

    suspend fun update(member: MemberEntity) {
        dao.insert(member)
    }

    suspend fun delete(member: MemberEntity) {
        dao.delete(member)
    }

    fun searchMembers(query: String): Flow<List<MemberEntity>> {
        return dao.searchByNameOrJabatan("%$query%")
    }

    fun getActiveMembers(): Flow<List<MemberEntity>> = dao.getActive()

    suspend fun isMemberExists(noAnggota: String): Boolean {
        return dao.countByNoAnggota(noAnggota) > 0
    }
}