package ru.developer.amukovozov.testapp.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "name")
    val cityName: String,
    @Json(name = "main")
    val mainWeather: MainWeather,
    @Json(name = "weather")
    val weather: List<Weather>
)