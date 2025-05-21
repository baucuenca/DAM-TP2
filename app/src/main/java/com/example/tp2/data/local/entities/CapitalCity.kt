package com.example.tp2.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "capital_cities")
data class CapitalCity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "country_name") val countryName: String,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "population") val population: Int
)