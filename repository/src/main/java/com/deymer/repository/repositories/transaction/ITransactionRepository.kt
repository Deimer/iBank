package com.deymer.repository.repositories.transaction

import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionModel
import com.deymer.repository.utils.OnResult

interface ITransactionRepository {

    suspend fun createTransaction(
        accountId: String,
        transaction: SimpleTransactionModel
    ): OnResult<Boolean>

    suspend fun fetchTransaction(
        transactionId: String
    ): OnResult<TransactionModel>

    suspend fun fetchTransactions(
        accountId: String
    ): OnResult<List<TransactionModel>>
}