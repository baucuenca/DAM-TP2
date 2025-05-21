package com.example.tp2.data.repositories

import android.content.Context
import com.example.tp2.data.local.dao.CapitalCityDao
import com.example.tp2.data.local.database.AppDatabase
import com.example.tp2.data.local.entities.CapitalCity
import kotlinx.coroutines.flow.Flow

class CapitalCityRepository(private val context: Context) {
    private val capitalCityDao: CapitalCityDao = AppDatabase.getDatabase(context).capitalCityDao()

    suspend fun insertCity(city: CapitalCity) {
        capitalCityDao.insert(city)
    }

    suspend fun updateCity(city: CapitalCity) {
        capitalCityDao.update(city)
    }

    suspend fun deleteCity(city: CapitalCity) {
        capitalCityDao.delete(city)
    }

    suspend fun getCityByName(cityName: String): CapitalCity? {
        return capitalCityDao.getCityByName(cityName)
    }

    suspend fun deleteCityByName(cityName: String) {
        capitalCityDao.deleteCityByName(cityName)
    }

    suspend fun deleteCitiesByCountry(countryName: String) {
        capitalCityDao.deleteCitiesByCountry(countryName)
    }

    fun getAllCities(): Flow<List<CapitalCity>> {
        return capitalCityDao.getAllCities()
    }
}