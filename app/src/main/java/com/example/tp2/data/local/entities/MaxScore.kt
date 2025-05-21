package com.example.tp2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="max_score")
data class MaxScore(
    @PrimaryKey val id: Int = 1,
    val value: Int
)