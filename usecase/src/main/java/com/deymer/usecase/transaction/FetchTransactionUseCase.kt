package com.deymer.usecase.transaction

import com.deymer.repository.repositories.transaction.ITransactionRepository
import javax.inject.Inject

class FetchTransactionUseCase @Inject constructor(
    private val transactionRepository: ITransactionRepository
) {

    suspend fun invoke(
        transactionId: String
    ) = transactionRepository.fetchTransaction(
        transactionId
    )
}