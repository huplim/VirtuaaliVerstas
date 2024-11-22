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
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = RetrofitInstance.weatherApiService.getWeatherData()
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