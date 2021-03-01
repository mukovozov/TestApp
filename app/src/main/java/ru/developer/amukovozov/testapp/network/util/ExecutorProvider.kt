package ru.developer.amukovozov.testapp.network.util

import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class ExecutorProvider {

    open fun getMainThreadExecutor(): Executor = MainThreadExecutor()

    open fun getSingleThreadExecutor(): Executor = Executors.newSingleThreadExecutor()
}