package com.example.virtuaaliverstas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

@Composable
fun WeatherAppHomeScreen(navController: NavHostController) {
    var weatherData by remember { mutableStateOf<WeatherData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val data = RetrofitInstance.weatherApiService.getWeatherData()
                withContext(Dispatchers.Main) {
                    weatherData = data
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    weatherData = null
                }
            }
        }
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
            text = weatherData?.name ?: "-",
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (weatherData != null) {
            Text(
                text = "${stringResource(id = R.string.description)}:" +
                        " ${weatherData?.weather?.getOrNull(0)?.main ?: "-"}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${stringResource(id = R.string.temperature)}:" +
                        " ${weatherData?.main?.temp ?: "-"}°C",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${stringResource(id = R.string.feels_like)}:" +
                        " ${weatherData?.main?.feelsLike ?: "-"}°C",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${stringResource(id = R.string.wind_speed)}:" +
                        " ${weatherData?.wind?.speed ?: "-"} m/s",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${stringResource(id = R.string.humidity)}:" +
                        " ${weatherData?.main?.humidity ?: "-"}%",
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            Text(text = stringResource(id = R.string.load_weather))
        }
    }
}

interface WeatherApiService {
    @GET("/data/2.5/weather?q=Tampere&APPID=c39ca0dc2b2e58cdb6689d8f4f8f2d25&units=metric")
    suspend fun getWeatherData(): WeatherData
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