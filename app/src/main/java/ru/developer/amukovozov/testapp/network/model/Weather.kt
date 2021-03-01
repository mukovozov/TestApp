package ru.developer.amukovozov.testapp.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Weather(
    val description: String
)