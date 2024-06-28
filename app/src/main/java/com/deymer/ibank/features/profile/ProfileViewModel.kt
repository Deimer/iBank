package com.deymer.ibank.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deymer.ibank.ui.models.UIUserModel
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.account.FetchAccountUseCase
import com.deymer.usecase.user.FetchUserUseCase
import com.deymer.usecase.user.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileUiState {
    data object Loading: ProfileUiState()
    data object Success: ProfileUiState()
    data object Logout: ProfileUiState()
}

sealed class ProfileErrorState {
    data class Error(val message: String? = null): ProfileErrorState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val fetchUserUseCase: FetchUserUseCase,
    private val fetchAccountUseCase: FetchAccountUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    private val _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileUiState = _profileUiState.asStateFlow()

    private val _profileErrorState = MutableStateFlow<ProfileErrorState>(ProfileErrorState.Error())
    val profileErrorState = _profileErrorState.asStateFlow()

    private val _profileState = MutableStateFlow(UIUserModel())
    val profileState: StateFlow<UIUserModel> = _profileState.asStateFlow()

    init {
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch(mainDispatcher) {
            _profileUiState.emit(ProfileUiState.Loading)
            when(val result = fetchUserUseCase.invoke()) {
                is OnResult.Success -> {
                    _profileState.value = _profileState.value.copy(
                        shortName = result.data.simpleName,
                        fullName = "${result.data.name} ${result.data.surname}",
                        email = result.data.email,
                        urlPhoto = result.data.urlPhoto,
                    )
                    fetchAccount()
                }
                is OnResult.Error -> {
                    _profileErrorState.emit(ProfileErrorState.Error(
                        result.exception.message
                    ))
                }
            }
        }
    }

    private fun fetchAccount() {
        viewModelScope.launch(mainDispatcher) {
            when(val result = fetchAccountUseCase.invoke()) {
                is OnResult.Success -> {
                    _profileState.value = _profileState.value.copy(
                        accountNumber = result.data.number,
                        numberTransactions = result.data.transactions.count(),
                        createdAt = result.data.createdAt,
                    )
                    _profileUiState.emit(ProfileUiState.Success)
                }
                is OnResult.Error -> {
                    _profileErrorState.emit(ProfileErrorState.Error(
                        result.exception.message
                    ))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch(mainDispatcher) {
            _profileUiState.emit(ProfileUiState.Loading)
            when(val result = logoutUseCase.invoke()) {
                is OnResult.Success -> {
                    _profileUiState.emit(ProfileUiState.Logout)
                }
                is OnResult.Error -> {
                    _profileErrorState.emit(ProfileErrorState.Error(
                        result.exception.message
                    ))
                }
            }
        }
    }
}