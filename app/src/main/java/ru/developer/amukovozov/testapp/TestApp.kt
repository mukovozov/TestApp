package ru.developer.amukovozov.testapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.developer.amukovozov.testapp.di.homeModule
import ru.developer.amukovozov.testapp.di.networkModule
import ru.developer.amukovozov.testapp.di.profileModule

class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TestApp)
            modules(networkModule, homeModule, profileModule)
        }
    }
}