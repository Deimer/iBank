package com.deymer.ibank.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.account.FetchAccountUseCase
import com.deymer.usecase.account.UpdateBalanceUseCase
import com.deymer.usecase.transaction.CreateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RechargeUiState {
    data object Default: RechargeUiState()
    data object Loading: RechargeUiState()
}

sealed class RechargeErrorState {
    data object SuccessForm: RechargeErrorState()
    data object ErrorForm: RechargeErrorState()
    data class Error(val message: String? = null): RechargeErrorState()
}

data class AccountUiState(
    val id: String = "",
    val number: String = "",
    val balance: Float = 0f,
)

data class RechargeFormState(
    val amount: Float = 0f,
    val description: String = "",
)

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val fetchAccountUseCase: FetchAccountUseCase,
    private val updateBalanceUseCase: UpdateBalanceUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase
): ViewModel() {

    private val _rechargeUiState = MutableStateFlow<RechargeUiState>(RechargeUiState.Default)
    val rechargeUiState = _rechargeUiState.asStateFlow()

    private val _rechargeErrorState = MutableStateFlow<RechargeErrorState>(RechargeErrorState.Error())
    val rechargeErrorState = _rechargeErrorState.asStateFlow()

    private val _accountState = MutableStateFlow(AccountUiState())
    val accountState = _accountState.asStateFlow()

    private val _rechargeFormState = MutableStateFlow(RechargeFormState())
    val rechargeFormState = _rechargeFormState.asStateFlow()

    fun onAmountChange(amount: Float) {
        _rechargeFormState.value = _rechargeFormState.value.copy(amount = amount)
    }

    fun onDescriptionChange(description: String) {
        _rechargeFormState.value = _rechargeFormState.value.copy(description = description)
    }

    init {
        fetchAccount()
    }

    private fun fetchAccount() {
        viewModelScope.launch(mainDispatcher) {
            _rechargeUiState.emit(RechargeUiState.Loading)
            when(val result = fetchAccountUseCase.invoke()) {
                is OnResult.Success -> {
                    _accountState.value = _accountState.value.copy(
                        id = result.data.id,
                        number = result.data.number,
                        balance = result.data.balance
                    )
                    _rechargeUiState.emit(RechargeUiState.Default)
                }
                is OnResult.Error -> {
                    _rechargeErrorState.emit(RechargeErrorState.Error(
                        result.exception.message
                    ))
                }
            }
        }
    }

    fun updateBalance() {
        val formState = _rechargeFormState.value
        viewModelScope.launch(mainDispatcher) {
            when {
                formState.amount == 0f || formState.description.isEmpty() ->
                    _rechargeErrorState.emit(RechargeErrorState.ErrorForm)
                else -> {
                    _rechargeUiState.emit(RechargeUiState.Loading)
                    when(val result = updateBalanceUseCase.invoke(
                        accountId = _accountState.value.id,
                        rechargeValue = _rechargeFormState.value.amount,
                        currentBalance = _accountState.value.balance
                    )) {
                        is OnResult.Success -> {
                            createTransaction()
                        }
                        is OnResult.Error -> {
                            _rechargeErrorState.emit(RechargeErrorState.Error(
                                result.exception.message
                            ))
                        }
                    }
                }
            }
        }
    }

    private fun createTransaction() {
        viewModelScope.launch(mainDispatcher) {
            when(val result = createTransactionUseCase.invoke(
                accountId = _accountState.value.id,
                amount = _rechargeFormState.value.amount,
                description = _rechargeFormState.value.description
            )) {
                is OnResult.Success -> {
                    _rechargeFormState.value = _rechargeFormState.value.copy(
                        description = "",
                        amount = 0f
                    )
                    _rechargeUiState.emit(RechargeUiState.Default)
                    _rechargeErrorState.emit(RechargeErrorState.SuccessForm)
                }
                is OnResult.Error -> {
                    _rechargeErrorState.emit(RechargeErrorState.Error(
                        result.exception.message
                    ))
                }
            }
        }
    }
}