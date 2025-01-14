package com.example.virtuaaliverstas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@Composable
fun WeatherAppHomeScreen(navController: NavHostController,
                         weatherViewModel: WeatherViewModel = viewModel(),
                         locationViewModel: LocationViewModel = viewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val apiKey = weatherViewModel.storedApiKey.collectAsState()
    val weatherData = weatherViewModel.currentWeatherData.collectAsState()
    val storedPlace = weatherViewModel.storedPlace.collectAsState()
    val useCurrentLocation = weatherViewModel.useCurrentLocation.collectAsState()

    val place = weatherData.value?.name ?: "-"
    val description = weatherData.value?.weather?.getOrNull(0)?.main ?: "-"
    val temperature = weatherData.value?.main?.temp ?: "-"
    val feelsLike = weatherData.value?.main?.feelsLike ?: "-"
    val windSpeed = weatherData.value?.wind?.speed ?: "-"
    val humidity = weatherData.value?.main?.humidity ?: "-"

    val coordinates = locationViewModel.locationData.collectAsState()

    var latitude = coordinates.value.first
    var longitude = coordinates.value.second

    var inputPlace by remember { mutableStateOf("") }
    var savedPlace by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(locationViewModel.locationData) {
        latitude = coordinates.value.first
        longitude = coordinates.value.second
    }

    // Use current location as default if checked in the settings
    // Otherwise, use stored place from data store for weather info if it exists
    LaunchedEffect(storedPlace.value, useCurrentLocation.value) {
        if (useCurrentLocation.value) {
            weatherViewModel.fetchWeatherDataByCoordinates(apiKey.value, latitude, longitude)
        }
        else if (storedPlace.value.isNotEmpty()) {
            weatherViewModel.fetchWeatherDataByPlace(apiKey.value, storedPlace.value)
        }
    }

    fun updateByPlace() {
        savedPlace = inputPlace
        inputPlace = ""
        weatherViewModel.fetchWeatherDataByPlace(apiKey.value, savedPlace)
        keyboardController?.hide()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${stringResource(id = R.string.current_weather)}:",
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = place,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${stringResource(id = R.string.description)}: $description",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${stringResource(id = R.string.temperature)}: $temperature°C",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${stringResource(id = R.string.feels_like)}: $feelsLike°C",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${stringResource(id = R.string.wind_speed)}: $windSpeed m/s",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${stringResource(id = R.string.humidity)}: $humidity%",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = inputPlace,
            onValueChange = { inputPlace = it },
            label = { Text(text = stringResource(id = R.string.enter_place)) },
            singleLine = true,
            // Pressing done "executes the Use Place button"
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { updateByPlace() }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = {
                    weatherViewModel.fetchWeatherDataByCoordinates(apiKey.value, latitude, longitude)
                }
            ) {
                Text(text = stringResource(id = R.string.use_location))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    updateByPlace()
                }
            ) {
                Text(text = stringResource(id = R.string.use_place))
            }
        }
    }
}

interface WeatherApiService {
    @GET("/data/2.5/weather?units=metric")
    suspend fun getWeatherDataByPlace(
        @Query("APPID") apiKey: String,
        @Query("q") place: String
    ): WeatherData

    // Example of a request with latitude and longitude (Tampere)
    // https://api.openweathermap.org/data/2.5/weather?lat=61.4991&lon=23.7871&APPID=[INSERT API KEY HERE]&units=metric
    @GET("/data/2.5/weather?units=metric")
    suspend fun getWeatherDataByCoordinates(
        @Query("APPID") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): WeatherData
}

object RetrofitInstance {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}

// All the data from the API for potential future use
// First version uses:
//  - weather[0].main
//  - main.temp
//  - main.feels_like
//  - wind.speed
//  - main.humidity

// Check for nullable fields in case of missing data
data class WeatherData(
    val weather: List<Weather>?,
    val main: Main?,
    val wind: Wind?,
    val clouds: Clouds?,
    val sys: Sys?,
    val name: String?,
    val timezone: Int?
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class Main(
    val temp: Double?,

    // makes feels_like into a camelCase variable
    @SerializedName("feels_like")
    val feelsLike: Double?,

    @SerializedName("temp_min")
    val tempMin: Double?,
    @SerializedName("temp_max")
    val tempMax: Double?,
    val pressure: Int?,
    val humidity: Int?
)

data class Wind(
    val speed: Double?,
    val deg: Int?
)

data class Clouds(
    val all: Int?
)

data class Sys(
    val country: String?,
    val sunrise: Long?,
    val sunset: Long?
)