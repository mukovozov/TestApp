package ru.developer.amukovozov.testapp.network.model

data class WeatherRequestParams(
    val lang: String = "ru",
    val units: String = "metric"
)