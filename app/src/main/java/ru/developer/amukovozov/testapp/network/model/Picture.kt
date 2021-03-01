package ru.developer.amukovozov.testapp.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Picture(
    @Json(name = "download_url")
    val url: String,
    val author: String
)