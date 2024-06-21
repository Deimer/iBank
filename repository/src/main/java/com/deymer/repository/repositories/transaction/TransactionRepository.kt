package com.deymer.repository.repositories.transaction

import com.deymer.datasource.remote.transaction.ITransactionDataSource
import com.deymer.repository.mappers.toEntity
import com.deymer.repository.mappers.toModel
import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionModel
import com.deymer.repository.utils.OnResult
import com.deymer.repository.utils.RepositoryConstants.Tags.TAG_NULL
import com.google.firebase.firestore.FirebaseFirestoreException
import java.lang.Exception
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDataSource: ITransactionDataSource
): ITransactionRepository {

    override suspend fun createTransaction(
        accountId: String,
        transaction: SimpleTransactionModel
    ): OnResult<Boolean> {
        return try {
            OnResult.Success(transactionDataSource.createTransaction(
                transaction.toEntity(accountId)
            ))
        } catch (exception: FirebaseFirestoreException) {
            OnResult.Error(exception)
        } catch (exception: Exception) {
            OnResult.Error(exception)
        }
    }

    override suspend fun fetchTransaction(
        transactionId: String
    ): OnResult<TransactionModel> {
        return try {
            return transactionDataSource.getTransaction(transactionId)?.let {
                OnResult.Success(it.toModel())
            } ?: run { OnResult.Error(IllegalArgumentException(TAG_NULL)) }
        } catch (exception: FirebaseFirestoreException) {
            OnResult.Error(exception)
        } catch (exception: Exception) {
            OnResult.Error(exception)
        }
    }

    override suspend fun fetchTransactions(
        accountId: String
    ): OnResult<List<TransactionModel>> {
        return try {
            return transactionDataSource.getTransactions(accountId).let { entities ->
                OnResult.Success(entities.map { it.toModel() })
            }
        } catch (exception: FirebaseFirestoreException) {
            OnResult.Error(exception)
        } catch (exception: Exception) {
            OnResult.Error(exception)
        }
    }
}