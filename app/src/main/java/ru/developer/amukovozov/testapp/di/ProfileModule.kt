package ru.developer.amukovozov.testapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.developer.amukovozov.testapp.network.repository.WeatherRepository
import ru.developer.amukovozov.testapp.ui.profile.ProfileViewModel

val profileModule = module {

    single { WeatherRepository(get()) }

    viewModel { ProfileViewModel(get(), get()) }
}