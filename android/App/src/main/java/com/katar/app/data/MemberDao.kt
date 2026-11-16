package com.katar.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Query("SELECT * FROM members ORDER BY nama ASC")
    fun getAll(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE status_aktif = 1 ORDER BY nama ASC")
    fun getActive(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE nama LIKE :query OR jabatan LIKE :query ORDER BY nama ASC")
    fun searchByNameOrJabatan(query: String): Flow<List<MemberEntity>>

    @Query("SELECT COUNT(*) FROM members WHERE no_anggota = :noAnggota")
    suspend fun countByNoAnggota(noAnggota: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: MemberEntity)

    @Delete
    suspend fun delete(member: MemberEntity)
}