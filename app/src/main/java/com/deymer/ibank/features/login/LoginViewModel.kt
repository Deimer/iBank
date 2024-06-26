package com.deymer.ibank.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deymer.ibank.ui.utils.isValidEmail
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    data object Loading: LoginUiState()
    data object Success: LoginUiState()
    data object Default: LoginUiState()
}

sealed class LoginErrorState {
    data object FormError: LoginErrorState()
    data object EmailError: LoginErrorState()
    data object PasswordError: LoginErrorState()
    data class Error(val message: String? = null): LoginErrorState()
}

data class LoginFormState(
    val email: String = "",
    val password: String = "",
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val loginUseCase: LoginUseCase,
): ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Default)
    val loginUiState = _loginUiState.asStateFlow()

    private val _loginErrorState = MutableStateFlow<LoginErrorState>(LoginErrorState.Error())
    val loginErrorState = _loginErrorState.asStateFlow()

    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState: StateFlow<LoginFormState> = _loginFormState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _loginFormState.value = _loginFormState.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _loginFormState.value = _loginFormState.value.copy(password = newPassword)
    }

    fun login() {
        val formState = _loginFormState.value
        viewModelScope.launch(mainDispatcher) {
            when {
                formState.email.isBlank() && formState.password.isBlank() -> {
                    _loginErrorState.emit(LoginErrorState.FormError)
                }
                formState.email.isValidEmail().not() -> {
                    _loginErrorState.emit(LoginErrorState.EmailError)
                }
                formState.password.isValidEmail().not() -> {
                    _loginErrorState.emit(LoginErrorState.PasswordError)
                }
                else -> {
                    when (val result = loginUseCase.invoke(formState.email, formState.password)) {
                        is OnResult.Success -> {
                            _loginUiState.emit(LoginUiState.Success)
                        }
                        is OnResult.Error -> {
                            _loginUiState.emit(LoginUiState.Default)
                            _loginErrorState.emit(LoginErrorState.Error(result.exception.message))
                        }
                    }
                }
            }
        }
    }
}