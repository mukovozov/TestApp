package ru.developer.amukovozov.testapp.util

interface Event

data class MessageEvent(val message: String) : Event