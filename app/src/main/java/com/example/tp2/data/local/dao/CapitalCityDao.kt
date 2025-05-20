package com.example.tp2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tp2.data.local.entities.CapitalCity
import kotlinx.coroutines.flow.Flow

@Dao
interface CapitalCityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: CapitalCity)

    @Update
    suspend fun update(city: CapitalCity)

    @Delete
    suspend fun delete(city: CapitalCity)

    @Query("SELECT * FROM capital_cities WHERE city_name LIKE :cityName LIMIT 1")
    suspend fun getCityByName(cityName: String): CapitalCity?

    @Query("DELETE FROM capital_cities WHERE city_name = :cityName")
    suspend fun deleteCityByName(cityName: String)

    @Query("DELETE FROM capital_cities WHERE country_name = :countryName")
    suspend fun deleteCitiesByCountry(countryName: String)

    @Query("SELECT * FROM capital_cities ORDER BY country_name ASC, city_name ASC")
    fun getAllCities(): Flow<List<CapitalCity>>
}