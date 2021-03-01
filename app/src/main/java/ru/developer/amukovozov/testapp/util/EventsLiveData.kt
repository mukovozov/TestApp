package ru.developer.amukovozov.testapp.util

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import java.util.*

class EventsLiveData : MutableLiveData<Queue<Event>>() {

    @MainThread
    fun offer(event: Event) {
        val queue = value ?: LinkedList()
        queue.add(event)
        value = queue
    }
}