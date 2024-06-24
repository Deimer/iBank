package com.deymer.ibank.features.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.account.CreateAccountUseCase
import com.deymer.usecase.user.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUserUseCase,
    private val createAccountUseCase: CreateAccountUseCase
): ViewModel() {

    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        photo: Uri?
    ) {
        photo?.let {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    when(val result = registerUseCase.invoke(
                        email, password, firstName, lastName, photo
                    )) {
                        is OnResult.Success -> {

                        }
                        is OnResult.Error -> {

                        }
                    }
                }
            }
        } ?: {

        }
    }

    fun createAccount() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when(val result = createAccountUseCase.invoke()) {
                    is OnResult.Success -> {

                    }
                    is OnResult.Error -> {

                    }
                }
            }
        }
    }

    fun setPhotoUri(uri: Uri) {

    }
}