package ru.developer.amukovozov.testapp.network.repository

import io.reactivex.rxjava3.core.Single
import ru.developer.amukovozov.testapp.network.api.WeatherApi
import ru.developer.amukovozov.testapp.network.model.WeatherRequestParams
import ru.developer.amukovozov.testapp.network.model.WeatherResponse

class WeatherRepository(private val weatherApi: WeatherApi) {

    fun getCityById(id: String): Single<WeatherResponse> {
        val params = WeatherRequestParams()
        return weatherApi.getWeatherByCityId(id, params.lang, params.units)
    }
}