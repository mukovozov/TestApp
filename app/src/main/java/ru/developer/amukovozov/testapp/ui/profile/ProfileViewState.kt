package ru.developer.amukovozov.testapp.ui.profile

import android.content.Context
import ru.developer.amukovozov.testapp.R

data class ProfileViewState(
    val loginField: Field = Field.Empty,
    val passwordField: Field = Field.Empty,
    val isButtonEnabled: Boolean = false,
    val isWeatherLoading: Boolean = false
)

fun loginValidationError(fieldValue: String, context: Context) =
    Field.ValidationError(fieldValue, context.getString(R.string.email_validation_error))

fun passwordValidationError(fieldValue: String, context: Context) =
    Field.ValidationError(fieldValue, context.getString(R.string.password_validation_error))

sealed class Field {
    data class Content(val fieldValue: String) : Field()
    object Empty : Field()
    data class ValidationError(val fieldValue: String, val error: String) : Field()

    fun extractValue(): String {
        return when (this) {
            is Content -> fieldValue
            is ValidationError -> fieldValue
            else -> ""
        }
    }
}