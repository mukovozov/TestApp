package ru.developer.amukovozov.testapp.network.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.developer.amukovozov.testapp.network.model.Picture

interface PicturesApi {
    @GET("list")
    fun getPictures(@Query("page") page: Int, @Query("limit") limit: Int): Single<List<Picture>>
}