package com.katar.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members")
    fun getAll(): Flow<List<MemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: MemberEntity)

    @Delete
    suspend fun delete(member: MemberEntity)
}