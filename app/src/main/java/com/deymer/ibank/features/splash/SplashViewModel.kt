package com.deymer.ibank.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.user.InSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashUiState(
    val errorMessage: String? = null,
    val inSession: Boolean = false
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val inSessionUseCase: InSessionUseCase
): ViewModel() {

    private val _splashUiState = MutableStateFlow(SplashUiState())
    val splashUiState = _splashUiState.asStateFlow()

    init {
        isActiveSession()
    }

    private fun isActiveSession() {
        viewModelScope.launch(mainDispatcher) {
            when(val result = inSessionUseCase.invoke()) {
                is OnResult.Success -> {
                    _splashUiState.value = _splashUiState.value.copy(inSession = result.data)
                }
                is OnResult.Error -> {
                    _splashUiState.value = _splashUiState.value.copy(errorMessage = result.exception.message)
                }
            }
        }
    }
}