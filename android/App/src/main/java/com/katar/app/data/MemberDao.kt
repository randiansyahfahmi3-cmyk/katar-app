package com.katar.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMember(member: MemberEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMember(member: MemberEntity)

    @Query("DELETE FROM members")
    suspend fun deleteAllMembers()

    @Query("DELETE FROM members WHERE id = :id")
    suspend fun deleteMemberById(id: Int)

    @Query("SELECT * FROM members")
    Flow<List<MemberEntity>> getAllMembers()

    @Query("SELECT * FROM members WHERE status_aktif = 1")
    Flow<List<MemberEntity>> getActiveMembers()

    @Query("SELECT * FROM members WHERE jabatan LIKE :jabatan")
    suspend fun searchByJabatan(jabatan: String): List<MemberEntity>

    @Query("SELECT * FROM members WHERE nama LIKE :nama OR jabatan LIKE :jabatan")
    suspend fun searchByNameOrJabatan(nama: String, jabatan: String): List<MemberEntity>

    @Query("SELECT * FROM members WHERE (nama LIKE :nama OR jabatan LIKE :jabatan) AND status_aktif = 1")
    suspend fun searchActiveByNameOrJabatan(nama: String, jabatan: String): List<MemberEntity>
}