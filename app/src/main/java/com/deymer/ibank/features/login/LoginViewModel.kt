package com.deymer.ibank.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
): ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginUiState.value = LoginUiState(credentialsError = true)
            return
        }
        viewModelScope.launch {
            _loginUiState.value = LoginUiState(isLoading = true)
            withContext(Dispatchers.IO) {
                when (val result = loginUseCase.invoke(email, password)) {
                    is OnResult.Success -> {
                        _loginUiState.value = LoginUiState(isLoginSuccessful = true)
                    }
                    is OnResult.Error -> {
                        _loginUiState.value = LoginUiState(error = result.exception.message)
                    }
                }
            }
        }
    }

    fun clearError() {
        _loginUiState.value = _loginUiState.value.copy(error = null)
    }

    fun clearCredentialsError() {
        _loginUiState.value = _loginUiState.value.copy(credentialsError = false)
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val credentialsError: Boolean = false,
    val error: String? = null
)