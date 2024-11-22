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

    init {
        fetchWeatherDataByPlace()
    }

    fun fetchWeatherDataByPlace(place: String = "Finland") {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = RetrofitInstance
                    .weatherApiService
                    .getWeatherDataByPlace(place)
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

    fun fetchWeatherDataByCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = RetrofitInstance
                    .weatherApiService
                    .getWeatherDataByCoordinates(latitude, longitude)
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