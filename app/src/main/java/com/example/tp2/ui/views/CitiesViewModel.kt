package com.example.tp2.ui.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.tp2.data.repositories.CapitalCityRepository
import com.example.tp2.data.local.dao.CapitalCityDao
import com.example.tp2.data.local.entities.CapitalCity

class CitiesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CapitalCityRepository(application)

    private val _allCities = MutableStateFlow<List<CapitalCity>>(emptyList())
    val allCities: StateFlow<List<CapitalCity>> = _allCities.asStateFlow()

    val countryInput = MutableStateFlow("")
    val cityInput = MutableStateFlow("")
    val populationInput = MutableStateFlow("")

    private val _message = MutableLiveData<String?>(null)
    val message: LiveData<String?> = _message

    private val _queriedCity = MutableLiveData<CapitalCity?>(null)
    val queriedCity: LiveData<CapitalCity?> = _queriedCity

    private val _showModifyDialog = MutableLiveData(false)
    val showModifyDialog: LiveData<Boolean> = _showModifyDialog

    private val _cityToModify = MutableLiveData<CapitalCity?>(null)
    val cityToModify: LiveData<CapitalCity?> = _cityToModify

    init {
        viewModelScope.launch {
            repository.getAllCities().collectLatest { cities ->
                _allCities.value = cities
            }
        }
    }

    fun addCity() {
        val country = countryInput.value.trim()
        val city = cityInput.value.trim()
        val population = populationInput.value.toIntOrNull()

        if (country.isEmpty() || city.isEmpty() || population == null || population <= 0) {
            _message.value = "Por favor, complete todos los campos correctamente."
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val existingCity = repository.getCityByName(city)
                if (existingCity != null && existingCity.countryName == country) {
                    _message.postValue("La ciudad capital '$city' de '$country' ya existe.")
                } else {
                    val newCity = CapitalCity(countryName = country, cityName = city, population = population)
                    repository.insertCity(newCity)
                    _message.postValue("Ciudad capital '$city' añadida.")
                    clearInputs()
                }
            }
        }
    }

    fun queryCity(cityName: String) {
        if (cityName.trim().isEmpty()) {
            _message.value = "Por favor, ingrese el nombre de la ciudad a consultar."
            _queriedCity.value = null
            return
        }
        viewModelScope.launch {
            val city = withContext(Dispatchers.IO) {
                repository.getCityByName(cityName.trim())
            }
            _queriedCity.value = city
            if (city == null) {
                _message.value = "Ciudad capital '$cityName' no encontrada."
            } else {
                _message.value = "Ciudad encontrada: ${city.cityName}, ${city.countryName}, ${city.population} hab."
            }
        }
    }

    fun deleteCityByName(cityName: String) {
        if (cityName.trim().isEmpty()) {
            _message.value = "Por favor, ingrese el nombre de la ciudad a borrar."
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val existingCity = repository.getCityByName(cityName.trim())
                if (existingCity != null) {
                    repository.deleteCityByName(cityName.trim())
                    _message.postValue("Ciudad capital '$cityName' borrada.")
                } else {
                    _message.postValue("Ciudad capital '$cityName' no encontrada para borrar.")
                }
            }
        }
    }

    fun deleteCitiesByCountry(countryName: String) {
        if (countryName.trim().isEmpty()) {
            _message.value = "Por favor, ingrese el nombre del país para borrar sus ciudades."
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteCitiesByCountry(countryName.trim())
                _message.postValue("Todas las ciudades de '$countryName' han sido borradas.")
            }
        }
    }

    fun showModifyDialog(city: CapitalCity) {
        _cityToModify.value = city
        populationInput.value = city.population.toString()
        _showModifyDialog.value = true
    }

    fun dismissModifyDialog() {
        _showModifyDialog.value = false
        _cityToModify.value = null
        populationInput.value = "" // Clear population input
    }

    fun modifyPopulation(city: CapitalCity, newPopulation: String) {
        val pop = newPopulation.toIntOrNull()
        if (pop == null || pop <= 0) {
            _message.value = "Por favor, ingrese una población válida para modificar."
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val updatedCity = city.copy(population = pop)
                repository.updateCity(updatedCity)
                _message.postValue("Población de '${city.cityName}' modificada a $pop.")
                dismissModifyDialog()
            }
        }
    }

    fun clearInputs() {
        countryInput.value = ""
        cityInput.value = ""
        populationInput.value = ""
    }

    fun clearMessage() {
        _message.value = null
    }
}