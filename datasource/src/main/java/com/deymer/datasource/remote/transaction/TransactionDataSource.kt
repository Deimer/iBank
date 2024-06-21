package com.deymer.datasource.remote.transaction

import com.deymer.database.DataBaseConstants.Tables.TRANSACTIONS_TABLE
import com.deymer.database.entities.TransactionEntity
import com.deymer.database.managers.firestore.IFirestoreManager
import com.deymer.datasource.DataSourceConstants.KEY_ACCOUNT_ID
import javax.inject.Inject

class TransactionDataSource @Inject constructor(
    private val firestoreManager: IFirestoreManager
): ITransactionDataSource {

    override suspend fun createTransaction(
        transaction: TransactionEntity
    ) = firestoreManager.save(transaction)

    override suspend fun createTransactions(
        transactions: List<TransactionEntity>
    ) = firestoreManager.save(transactions)

    override suspend fun getTransaction(
        transactionId: String
    ): TransactionEntity? {
        return firestoreManager.getById(
            transactionId,
            TRANSACTIONS_TABLE
        )?.let {
            it.toObject(TransactionEntity::class.java)?.apply {
                documentId = it.id
            }
        }
    }

    override suspend fun getTransactions(
        accountId: String
    ): List<TransactionEntity> {
        return firestoreManager.getListByValue(
            KEY_ACCOUNT_ID,
            accountId,
            TRANSACTIONS_TABLE
        ).mapNotNull { it.toObject(
            TransactionEntity::class.java
        )?.apply { documentId = it.id } }.sortedBy { it.createdAt }
    }
}