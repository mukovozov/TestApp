package ru.developer.amukovozov.testapp.util

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseViewModel : ViewModel() {
    val events = EventsLiveData()
    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    protected fun showMessage(message: String) {
        val messageEvent = MessageEvent(message)
        events.offer(messageEvent)
    }

    protected fun Disposable.disposeOnViewModelDestroy(): Disposable {
        compositeDisposable.add(this)
        return this
    }
}