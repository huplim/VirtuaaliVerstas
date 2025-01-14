package com.example.virtuaaliverstas

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

val WEATHER_KEY = stringPreferencesKey("weather_place")
val LOCATION_KEY = stringPreferencesKey("location_use")
val API_KEY = stringPreferencesKey("api_key")

suspend fun savePlace(context: Context, place: String) {
    context.dataStore.edit { prefs ->
        prefs[WEATHER_KEY] = place
    }
}

suspend fun getPlace(context: Context): String {
    val prefs = context.dataStore.data.first()
    return prefs[WEATHER_KEY] ?: "-"
}

suspend fun updateUseLocation(context: Context, useLocation: Boolean) {
    context.dataStore.edit { prefs ->
        prefs[LOCATION_KEY] = useLocation.toString()
    }
}

suspend fun getUseLocation(context: Context): Boolean {
    val prefs = context.dataStore.data.first()
    return prefs[LOCATION_KEY]?.toBoolean() ?: false
}

suspend fun saveApiKey(context: Context, apiKey: String) {
    context.dataStore.edit { prefs ->
        prefs[API_KEY] = apiKey
    }
}

suspend fun getApiKey(context: Context): String {
    val prefs = context.dataStore.data.first()
    return prefs[API_KEY] ?: "-"
}