package com.example.virtuaaliverstas

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val Context.weatherDataStore: DataStore<Preferences> by preferencesDataStore(name = "weather_data")

object PreferencesKeys {
    val WEATHER_PLACE = stringPreferencesKey("weather_place")
}