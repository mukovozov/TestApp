package ru.developer.amukovozov.testapp.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import java.util.*

fun LifecycleOwner.observe(eventsLiveData: EventsLiveData, eventHandler: (Event) -> Unit) {
    eventsLiveData.observe(
        this,
        { queue: Queue<Event>? ->
            while (queue != null && queue.isNotEmpty()) {
                eventHandler(queue.poll())
            }
        }
    )
}

inline fun <reified T : Any, reified L : LiveData<T>> LifecycleOwner.observe(
    liveData: L?,
    noinline block: (T) -> Unit
) {
    liveData?.observe(this, block::invoke)
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
