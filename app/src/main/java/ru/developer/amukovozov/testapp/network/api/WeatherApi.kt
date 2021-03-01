package ru.developer.amukovozov.testapp.network.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.developer.amukovozov.testapp.network.model.WeatherResponse

interface WeatherApi {
    @GET("weather")
    fun getWeatherByCityId(
        @Query("id") cityId: String,
        @Query("lang") lang: String,
        @Query("units") units: String
    ): Single<WeatherResponse>
}