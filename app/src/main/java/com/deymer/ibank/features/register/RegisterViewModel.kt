package com.deymer.ibank.features.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deymer.ibank.ui.utils.isValidEmail
import com.deymer.ibank.ui.utils.isValidPassword
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.account.CreateAccountUseCase
import com.deymer.usecase.user.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RegisterUiState {
    data object Loading: RegisterUiState()
    data object Success: RegisterUiState()
    data object Default: RegisterUiState()
}

sealed class RegisterErrorState {
    data object FormError: RegisterErrorState()
    data object EmailError: RegisterErrorState()
    data object PasswordError: RegisterErrorState()
    data object DifferentPasswordsError: RegisterErrorState()
    data object FirstNameError: RegisterErrorState()
    data object LastNameError: RegisterErrorState()
    data class Error(val message: String? = null): RegisterErrorState()
}

data class RegisterFormState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val isEmpty: Boolean = email.isBlank()
            && password.isBlank()
            && confirmPassword.isBlank()
            && firstName.isBlank()
            && lastName.isBlank()
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val registerUseCase: RegisterUserUseCase,
    private val createAccountUseCase: CreateAccountUseCase
): ViewModel() {

    private val _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Default)
    val registerUiState = _registerUiState.asStateFlow()

    private val _registerErrorState = MutableStateFlow<RegisterErrorState>(RegisterErrorState.Error())
    val registerErrorState = _registerErrorState.asStateFlow()

    private val _registerFormState = MutableStateFlow(RegisterFormState())
    val registerFormState = _registerFormState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _registerFormState.value = _registerFormState.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _registerFormState.value = _registerFormState.value.copy(password = newPassword)
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _registerFormState.value = _registerFormState.value.copy(confirmPassword = newConfirmPassword)
    }

    fun onFirstNameChange(newFirstName: String) {
        _registerFormState.value = _registerFormState.value.copy(firstName = newFirstName)
    }

    fun onLastNameChange(newLastName: String) {
        _registerFormState.value = _registerFormState.value.copy(lastName = newLastName)
    }

    fun register() {
        val formState = _registerFormState.value
        viewModelScope.launch(mainDispatcher) {
            when {
                formState.isEmpty -> {
                    _registerErrorState.emit(RegisterErrorState.FormError)
                }
                formState.email.isValidEmail().not() ->
                    _registerErrorState.emit(RegisterErrorState.EmailError)
                formState.password.isValidPassword().not() ->
                    _registerErrorState.emit(RegisterErrorState.PasswordError)
                formState.password != formState.confirmPassword ->
                    _registerErrorState.emit(RegisterErrorState.DifferentPasswordsError)
                formState.firstName.isEmpty() ->
                    _registerErrorState.emit(RegisterErrorState.FirstNameError)
                formState.lastName.isEmpty() ->
                    _registerErrorState.emit(RegisterErrorState.LastNameError)
                else -> {
                    _registerUiState.emit(RegisterUiState.Loading)
                    viewModelScope.launch {
                        when(val result = registerUseCase.invoke(
                            formState.email, formState.password, formState.firstName, formState.lastName, Uri.parse("")
                        )) {
                            is OnResult.Success -> {
                                createAccount()
                            }
                            is OnResult.Error -> {
                                _registerUiState.emit(RegisterUiState.Default)
                                _registerErrorState.emit(RegisterErrorState.Error(
                                    result.exception.message.orEmpty()
                                ))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createAccount() {
        viewModelScope.launch {
            when(val result = createAccountUseCase.invoke()) {
                is OnResult.Success -> {
                    _registerUiState.emit(RegisterUiState.Success)
                }
                is OnResult.Error -> {
                    _registerUiState.emit(RegisterUiState.Default)
                    _registerErrorState.emit(RegisterErrorState.Error(
                        result.exception.message.orEmpty()
                    ))
                }
            }
        }
    }
}