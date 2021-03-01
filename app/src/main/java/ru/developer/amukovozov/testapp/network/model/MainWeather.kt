package ru.developer.amukovozov.testapp.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MainWeather(
    val temp: String,
    val humidity: String
)