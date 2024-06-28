package com.deymer.ibank.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deymer.repository.models.TransactionModel
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.account.FetchAccountUseCase
import com.deymer.usecase.user.FetchUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeUiState {
    data object Loading: HomeUiState()
    data object Success: HomeUiState()
}

sealed class HomeErrorState {
    data class Error(val message: String? = null): HomeErrorState()
}

data class AccountUiState(
    val balance: Float = 0f,
    val currency: String = "",
    val transactions: List<TransactionModel> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val fetchUserUseCase: FetchUserUseCase,
    private val fetchAccountUseCase: FetchAccountUseCase
): ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    private val _homeErrorState = MutableStateFlow<HomeErrorState>(HomeErrorState.Error())
    val homeErrorState = _homeErrorState.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _accountUiState = MutableStateFlow(AccountUiState())
    val accountUiState: StateFlow<AccountUiState> = _accountUiState.asStateFlow()

    init {
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch(mainDispatcher) {
            _homeUiState.emit(HomeUiState.Loading)
            when(val result = fetchUserUseCase.invoke()) {
                is OnResult.Success -> {
                    _userName.emit(result.data.simpleName)
                    fetchAccount()
                }
                is OnResult.Error -> {
                    _homeErrorState.emit(HomeErrorState.Error(
                        result.exception.message.orEmpty()
                    ))
                }
            }
        }
    }

    private fun fetchAccount() {
        viewModelScope.launch(mainDispatcher) {
            when(val result = fetchAccountUseCase.invoke()) {
                is OnResult.Success -> {
                    _accountUiState.value = _accountUiState.value.copy(
                        balance = result.data.balance,
                        currency = result.data.currency.name,
                        transactions = result.data.transactions
                    )
                    _homeUiState.emit(HomeUiState.Success)
                }
                is OnResult.Error -> {
                    _homeErrorState.emit(HomeErrorState.Error(
                        result.exception.message.orEmpty()
                    ))
                }
            }
        }
    }
}