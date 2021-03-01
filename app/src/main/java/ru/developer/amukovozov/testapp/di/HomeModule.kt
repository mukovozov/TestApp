package ru.developer.amukovozov.testapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.developer.amukovozov.testapp.network.repository.PicturesRepository
import ru.developer.amukovozov.testapp.network.util.ExecutorProvider
import ru.developer.amukovozov.testapp.network.util.pagination.PagedListFactory
import ru.developer.amukovozov.testapp.ui.home.HomeViewModel

val homeModule = module {
    single { PicturesRepository(get()) }
    single { ExecutorProvider() }
    single { PagedListFactory(get()) }

    viewModel { HomeViewModel(get(), get()) }
}