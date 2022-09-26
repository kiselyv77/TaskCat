package com.example.tasksapp.data.local

import androidx.room.*
import com.example.tasksapp.data.local.entity.TokenEntity


@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(
        tokenEntity: TokenEntity
    )

    @Query("SELECT * FROM tokenentity WHERE id=:id")
    suspend fun getToken(id:String = "token"): TokenEntity

    @Query("DELETE FROM tokenentity WHERE id = :id")
    suspend fun deleteToken(id:String = "token")
}