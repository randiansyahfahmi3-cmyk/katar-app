package com.katar.app.data

import kotlinx.coroutines.flow.Flow

class MemberRepository(private val dao: MemberDao) {
    fun getAll(): Flow<List<MemberEntity>> = dao.getAll()
    suspend fun insert(member: MemberEntity) = dao.insert(member)
    suspend fun delete(member: MemberEntity) = dao.delete(member)
}