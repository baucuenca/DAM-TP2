package com.example.tp2.data.repositories

import android.app.Application
import com.example.tp2.data.local.dao.ScoreDao
import com.example.tp2.data.local.database.AppDatabase
import com.example.tp2.data.local.entities.MaxScore
import kotlinx.coroutines.flow.Flow

class ScoreRepository(application: Application) {
    private val scoreDao: ScoreDao

    init {
        val database = AppDatabase.getDatabase(application)
        scoreDao = database.scoreDao()
    }

    fun getMaxScore(): Flow<MaxScore?> {
        return scoreDao.getMaxScore()
    }

    suspend fun insertMaxScore(score: MaxScore) {
        scoreDao.insertMaxScore(score)
    }
}