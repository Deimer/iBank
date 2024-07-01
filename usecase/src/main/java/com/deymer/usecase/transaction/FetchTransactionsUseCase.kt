package com.deymer.usecase.transaction

import com.deymer.repository.repositories.transaction.ITransactionRepository
import javax.inject.Inject

class FetchTransactionsUseCase @Inject constructor(
    private val transactionRepository: ITransactionRepository
) {

    suspend fun invoke(accountId: String) =
        transactionRepository.fetchTransactions(accountId)
}