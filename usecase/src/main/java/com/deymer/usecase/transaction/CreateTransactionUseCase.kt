package com.deymer.usecase.transaction

import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.transaction.ITransactionRepository
import com.deymer.repository.utils.OnResult
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val transactionRepository: ITransactionRepository
) {

    suspend fun invoke(
        accountId: String,
        amount: Float,
        type: String,
        description: String
    ): OnResult<Boolean> {
        val transaction = SimpleTransactionModel(
            amount = amount,
            type = TransactionType.valueOf(type),
            description = description
        )
        return transactionRepository.createTransaction(
            accountId, transaction
        )
    }
}