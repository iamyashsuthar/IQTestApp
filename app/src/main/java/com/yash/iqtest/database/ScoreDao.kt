package com.yash.iqtest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {

    @Insert
    suspend fun insertScore(score: ScoreEntity)

    @Query("SELECT * FROM scores ORDER BY id DESC")
    suspend fun getScores(): List<ScoreEntity>
}
