package com.deymer.ibank.features.transaction.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deymer.repository.utils.OnResult
import com.deymer.repository.utils.RepositoryConstants.Tags.TAG_NULL
import com.deymer.usecase.account.FetchAccountByNumberUseCase
import com.deymer.usecase.account.FetchAccountUseCase
import com.deymer.usecase.account.TransferAmountUseCase
import com.deymer.usecase.transaction.CreateTransferTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TransferUiState {
    data object Default: TransferUiState()
    data object Loading: TransferUiState()
}

sealed class TransferErrorState {
    data object SuccessForm: TransferErrorState()
    data object ErrorForm: TransferErrorState()
    data object ErrorAccount: TransferErrorState()
    data class Error(val message: String? = null): TransferErrorState()
}

data class TransferFormState(
    val amount: Float = 0f,
    val accountNumberDestiny: String = "",
    val description: String = "",
)

data class AccountUiState(
    val id: String = "",
    val number: String = "",
    val balance: Float = 0f,
)

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val fetchAccountUseCase: FetchAccountUseCase,
    private val fetchAccountByNumberUseCase: FetchAccountByNumberUseCase,
    private val transferAmountUseCase: TransferAmountUseCase,
    private val createTransferTransactionsUseCase: CreateTransferTransactionsUseCase
): ViewModel() {

    private val _transferUiState = MutableStateFlow<TransferUiState>(TransferUiState.Default)
    val transferUiState = _transferUiState.asStateFlow()

    private val _transferErrorState = MutableStateFlow<TransferErrorState>(TransferErrorState.Error())
    val transferErrorState = _transferErrorState.asStateFlow()

    private val _transferFormState = MutableStateFlow(TransferFormState())
    val transferFormState = _transferFormState.asStateFlow()

    private val _accountState = MutableStateFlow(AccountUiState())
    val accountState = _accountState.asStateFlow()

    fun onAccountNumberDestinyChange(accountNumberDestiny: String) {
        _transferFormState.value = _transferFormState.value.copy(accountNumberDestiny = accountNumberDestiny)
    }

    fun onAmountChange(amount: Float) {
        _transferFormState.value = _transferFormState.value.copy(amount = amount)
    }

    fun onDescriptionChange(description: String) {
        _transferFormState.value = _transferFormState.value.copy(description = description)
    }

    init {
        fetchAccount()
    }

    private fun fetchAccount() {
        viewModelScope.launch(mainDispatcher) {
            _transferUiState.emit(TransferUiState.Loading)
            when(val result = fetchAccountUseCase.invoke()) {
                is OnResult.Success -> {
                    _accountState.value = _accountState.value.copy(
                        id = result.data.id,
                        number = result.data.number,
                        balance = result.data.balance
                    )
                    _transferUiState.emit(TransferUiState.Default)
                }
                is OnResult.Error -> {
                    _transferErrorState.emit(
                        TransferErrorState.Error(result.exception.message)
                    )
                }
            }
        }
    }

    fun validateAccount() {
        val formState = _transferFormState.value
        viewModelScope.launch(mainDispatcher) {
            when {
                formState.amount == 0f
                        || formState.accountNumberDestiny.isEmpty()
                        || formState.description.isEmpty() ->
                    _transferErrorState.emit(TransferErrorState.ErrorForm)
                formState.amount > accountState.value.balance ->
                    _transferErrorState.emit(TransferErrorState.ErrorForm)
                else -> {
                    _transferUiState.emit(TransferUiState.Loading)
                    when(val result = fetchAccountByNumberUseCase.invoke(
                        _transferFormState.value.accountNumberDestiny
                    )) {
                        is OnResult.Success -> {
                            transfer(
                                accountIdDestiny = result.data.id,
                                currentBalanceDestiny = result.data.balance
                            )
                        }
                        is OnResult.Error -> {
                            _transferUiState.emit(TransferUiState.Default)
                            when {
                                result.exception.message == TAG_NULL ->
                                    _transferErrorState.value = TransferErrorState.ErrorAccount
                                else ->
                                    _transferErrorState.emit(TransferErrorState.Error(
                                        result.exception.message)
                                    )
                            }
                        }
                    }
                }
            }
        }
    }

    fun transfer(
        accountIdDestiny: String,
        currentBalanceDestiny: Float
    ) {
        viewModelScope.launch(mainDispatcher) {
            when(val result = transferAmountUseCase.invoke(
                transferAmount = _transferFormState.value.amount,
                accountId = _accountState.value.id,
                currentBalance = _accountState.value.balance,
                accountIdDestiny = accountIdDestiny,
                currentBalanceDestiny = currentBalanceDestiny
            )) {
                is OnResult.Success -> {
                    createTransactions(accountIdDestiny)
                }
                is OnResult.Error -> {
                    _transferErrorState.emit(
                        TransferErrorState.Error(result.exception.message)
                    )
                }
            }
        }
    }

    private fun createTransactions(
        accountIdDestiny: String
    ) {
        viewModelScope.launch(mainDispatcher) {
            when(val result = createTransferTransactionsUseCase.invoke(
                accountId = _accountState.value.id,
                accountIdDestiny = accountIdDestiny,
                amount = _transferFormState.value.amount,
                description = _transferFormState.value.description
            )) {
                is OnResult.Success -> {
                    _transferFormState.value = _transferFormState.value.copy(
                        accountNumberDestiny = "",
                        description = "",
                        amount = 0f
                    )
                    _transferUiState.emit(TransferUiState.Default)
                    _transferErrorState.emit(TransferErrorState.SuccessForm)
                }
                is OnResult.Error -> {
                    _transferErrorState.emit(
                        TransferErrorState.Error(result.exception.message)
                    )
                }
            }
        }
    }
}