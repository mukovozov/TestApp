package ru.developer.amukovozov.testapp.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.developer.amukovozov.testapp.network.api.PicturesApi
import ru.developer.amukovozov.testapp.network.api.WeatherApi

private const val APP_ID = "c35880b49ff95391b3a6d0edd0c722eb"

val networkModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://picsum.photos/v2/")
            .client(
                OkHttpClient().newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PicturesApi::class.java)
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor { chain ->
                    val originalRequest = chain.request()

                    val modifiedUrl = originalRequest.url.toUrl().toString()
                        .plus("&appId=c35880b49ff95391b3a6d0edd0c722eb")

                    val modifiedRequest = originalRequest.newBuilder()
                        .url(modifiedUrl)
                        .build()

                    chain.proceed(modifiedRequest)
                }
                .build())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}