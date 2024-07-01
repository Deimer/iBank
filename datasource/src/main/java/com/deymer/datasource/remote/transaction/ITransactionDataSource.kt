package com.deymer.datasource.remote.transaction

import com.deymer.database.entities.TransactionEntity

interface ITransactionDataSource {

    suspend fun createTransaction(
        transaction: TransactionEntity
    ): Boolean

    suspend fun createTransactions(
        transactions: List<TransactionEntity>
    ): Boolean

    suspend fun getTransaction(
        transactionId: String
    ): TransactionEntity?

    suspend fun getTransactions(
        accountId: String
    ): List<TransactionEntity>
}