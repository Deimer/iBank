package com.deymer.usecase.transaction

import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.transaction.ITransactionRepository
import com.deymer.repository.utils.OnResult
import javax.inject.Inject

class CreateTransferTransactionsUseCase @Inject constructor(
    private val transactionRepository: ITransactionRepository
) {

    suspend fun invoke(
        accountId: String,
        accountIdDestiny: String,
        amount: Float,
        description: String
    ): OnResult<Boolean> {
        val transactionOrigin = SimpleTransactionModel(
            destinationAccountId = accountIdDestiny,
            amount = amount,
            type = TransactionType.TRANSFER,
            description = description
        )
        val originResult = transactionRepository.createTransaction(
            accountId,
            transactionOrigin
        )
        val transactionDestiny = SimpleTransactionModel(
            destinationAccountId = accountId,
            amount = amount,
            type = TransactionType.DEPOSIT,
            description = description
        )
        val destinyResult = transactionRepository.createTransaction(
            accountIdDestiny,
            transactionDestiny
        )
        return when {
            originResult is OnResult.Success && destinyResult is OnResult.Success ->
                OnResult.Success(true)
            originResult is OnResult.Error ->
                OnResult.Error(originResult.exception)
            else -> OnResult.Error(IllegalArgumentException(""))
        }
    }
}