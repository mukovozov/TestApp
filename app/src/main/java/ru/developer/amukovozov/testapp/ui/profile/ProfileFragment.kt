package ru.developer.amukovozov.testapp.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.focusChanges
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.developer.amukovozov.testapp.R
import ru.developer.amukovozov.testapp.databinding.FragmentProfileBinding
import ru.developer.amukovozov.testapp.util.BaseFragment
import ru.developer.amukovozov.testapp.util.MessageEvent
import ru.developer.amukovozov.testapp.util.hideKeyboard
import ru.developer.amukovozov.testapp.util.observe
import java.util.concurrent.TimeUnit

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    companion object {
        private const val DEBOUNCE_IN_MILLIS = 500L
    }

    private val viewModel: ProfileViewModel by viewModel()

    private val loadingDialog: AlertDialog by lazy {
        AlertDialog.Builder(context)
            .setTitle("Загружаю погоду")
            .setCancelable(false)
            .setMessage("Ожидайте")
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentProfileBinding.bind(view)

        initUi(binding)

        observe(viewModel.events) { event ->
            when (event) {
                is MessageEvent -> {
                    Snackbar.make(view, event.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
        observe(viewModel.viewState, { viewState -> render(binding, viewState) })
    }

    private fun render(
        binding: FragmentProfileBinding,
        viewState: ProfileViewState
    ) = with(binding) {
        button.isEnabled = viewState.isButtonEnabled

        val extractedLogin = viewState.loginField.extractValue()
        if (loginEdit.text.toString() != extractedLogin) {
            loginEdit.setText(extractedLogin)
        }
        login.error = (viewState.loginField as? Field.ValidationError)?.error ?: ""

        val extractedPassword = viewState.passwordField.extractValue()
        if (passwordEdit.text.toString() != extractedPassword) {
            passwordEdit.setText(extractedPassword)
        }
        password.error = (viewState.passwordField as? Field.ValidationError)?.error ?: ""

        if (viewState.isWeatherLoading) {
            loadingDialog.show()
        } else {
            loadingDialog.hide()
        }
    }

    private fun initUi(binding: FragmentProfileBinding) {
        binding.loginEdit.textChanges()
            .map(CharSequence::toString)
            .distinctUntilChanged()
            .debounce(DEBOUNCE_IN_MILLIS, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(viewModel::onLoginChanged)
            .disposeOnViewDestroy()

        binding.passwordEdit.textChanges()
            .map(CharSequence::toString)
            .distinctUntilChanged()
            .debounce(DEBOUNCE_IN_MILLIS, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(viewModel::onPasswordChanged)
            .disposeOnViewDestroy()

        binding.loginEdit.focusChanges()
            .skipInitialValue()
            .subscribe { viewModel.onLoginFocusChanged(binding.loginEdit.text.toString(), it) }
            .disposeOnViewDestroy()

        binding.passwordEdit.focusChanges()
            .skipInitialValue()
            .subscribe { viewModel.onPasswordFocusChanged(binding.passwordEdit.text.toString(), it) }
            .disposeOnViewDestroy()

        binding.button.setOnClickListener {
            activity?.hideKeyboard()
            viewModel.onButtonClicked()
        }
    }
}