package com.example.tp2.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.tp2.data.local.entities.MaxScore

@Dao
interface ScoreDao {
    @Query("SELECT * FROM max_score WHERE id = 1")
    fun getMaxScore(): Flow<MaxScore?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaxScore(score: MaxScore)
}