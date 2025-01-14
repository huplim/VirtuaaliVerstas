package com.example.virtuaaliverstas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(application : Application) : AndroidViewModel(application) {
    private val weatherData : WeatherData? = null
    var currentWeatherData = MutableStateFlow(weatherData)
    var storedPlace = MutableStateFlow("")
    var useCurrentLocation = MutableStateFlow(false)
    var storedApiKey = MutableStateFlow("")

    init {
        fetchSettingsFromDataStore()
    }

    private fun fetchSettingsFromDataStore() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                storedApiKey.value = getApiKey(getApplication())
                storedPlace.value = getPlace(getApplication())
                useCurrentLocation.value = getUseLocation(getApplication())
            }
        }
    }

    fun fetchWeatherDataByPlace(apiKey: String, place: String = "Finland") {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = RetrofitInstance
                    .weatherApiService
                    .getWeatherDataByPlace(apiKey, place)
                withContext(Dispatchers.Main) {
                    currentWeatherData.value = data
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    currentWeatherData.value = null
                }
            }
        }
    }

    fun fetchWeatherDataByCoordinates(apiKey: String, latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = RetrofitInstance
                    .weatherApiService
                    .getWeatherDataByCoordinates(apiKey, latitude, longitude)
                withContext(Dispatchers.Main) {
                    currentWeatherData.value = data
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    currentWeatherData.value = null
                }
            }
        }
    }
}