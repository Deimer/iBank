package com.deymer.ibank.features.transaction.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deymer.ibank.ui.models.UITransactionModel
import com.deymer.ibank.ui.utils.transactionWin
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.transaction.FetchTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DetailsUiState {
    data object Loading: DetailsUiState()
    data object Success: DetailsUiState()
}

sealed class DetailsErrorState {
    data class Error(val message: String? = null): DetailsErrorState()
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val mainDispatcher: CoroutineDispatcher,
    private val fetchTransactionUseCase: FetchTransactionUseCase
): ViewModel() {

    private val _detailsUiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val detailsUiState = _detailsUiState.asStateFlow()

    private val _detailsErrorState = MutableStateFlow<DetailsErrorState>(DetailsErrorState.Error())
    val detailsErrorState = _detailsErrorState.asStateFlow()

    private val _transactionState = MutableStateFlow(UITransactionModel())
    val transactionState: StateFlow<UITransactionModel> = _transactionState.asStateFlow()

    fun getTransaction(transactionId: String) {
        viewModelScope.launch(mainDispatcher) {
            when(val result = fetchTransactionUseCase.invoke(transactionId)) {
                is OnResult.Success -> {
                    _detailsUiState.emit(DetailsUiState.Success)
                    _transactionState.value = _transactionState.value.copy(
                        amount = result.data.amount.toString(),
                        type = result.data.type.name.lowercase().replaceFirstChar { it.uppercaseChar() },
                        isWin = transactionWin(result.data.type.name),
                        shortDate = result.data.shortDate,
                        fullDate = result.data.createdAt,
                        description = result.data.description,
                    )
                }
                is OnResult.Error -> {
                    _detailsErrorState.emit(
                        DetailsErrorState.Error(
                            result.exception.message
                        )
                    )
                }
            }
        }
    }
}