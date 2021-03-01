package ru.developer.amukovozov.testapp.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.developer.amukovozov.testapp.R
import ru.developer.amukovozov.testapp.network.repository.WeatherRepository
import ru.developer.amukovozov.testapp.util.BaseViewModel
import java.util.regex.Pattern

class ProfileViewModel(
    private val context: Context,
    private val repository: WeatherRepository
) : BaseViewModel() {
    companion object {
        private val emailPattern =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

        private val passwordPattern =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{6,}\$")

        private const val spbID = "498817"
    }

    val viewState = MutableLiveData<ProfileViewState>().apply {
        value = ProfileViewState()
    }

    fun onLoginChanged(login: String) {
        val isLoginValid = validate(login, emailPattern)
        val isPasswordValid = validate(viewState.value!!.passwordField.extractValue(), passwordPattern)

        viewState.value = viewState.value?.copy(
            loginField = Field.Content(login),
            isButtonEnabled = isLoginValid && isPasswordValid
        )
    }

    fun onPasswordChanged(password: String) {
        val isLoginValid = validate(viewState.value!!.loginField.extractValue(), emailPattern)
        val isPasswordValid = validate(password, passwordPattern)

        viewState.value = viewState.value?.copy(
            passwordField = Field.Content(password),
            isButtonEnabled = isLoginValid && isPasswordValid
        )
    }

    fun onLoginFocusChanged(login: String, hasFocus: Boolean) {
        val isEmailValid = validate(login, emailPattern)
        if (isEmailValid || hasFocus) {
            viewState.value = viewState.value?.copy(
                loginField = Field.Content(login),
                isButtonEnabled = isEmailValid && viewState.value?.passwordField is Field.Content
            )
        } else {
            viewState.value = viewState.value?.copy(
                loginField = Field.ValidationError(login, context.getString(R.string.email_validation_error)),
                isButtonEnabled = false
            )
        }
    }

    fun onPasswordFocusChanged(password: String, hasFocus: Boolean) {
        val isPasswordValid = validate(password, passwordPattern)
        if (isPasswordValid || hasFocus) {
            viewState.value = viewState.value?.copy(
                passwordField = Field.Content(password),
                isButtonEnabled = isPasswordValid && viewState.value?.loginField is Field.Content
            )
        } else {
            viewState.value = viewState.value?.copy(
                passwordField = Field.ValidationError(password, context.getString(R.string.password_validation_error)),
                isButtonEnabled = false
            )
        }
    }

    fun onButtonClicked() {
        repository.getCityById(spbID)
            .doOnSubscribe {
                viewState.postValue(viewState.value?.copy(isWeatherLoading = true))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    viewState.value = viewState.value?.copy(isWeatherLoading = false)
                    val message = context.getString(
                        R.string.weather_report,
                        response.cityName,
                        response.mainWeather.temp,
                        response.weather.firstOrNull()?.description,
                        response.mainWeather.humidity
                    )
                    showMessage(message)
                },
                { Log.e("TAG", it.toString()) }
            )
            .disposeOnViewModelDestroy()
    }

    private fun validate(fieldValue: String, pattern: Pattern): Boolean {
        val matcher = pattern.matcher(fieldValue)
        return matcher.find()
    }
}