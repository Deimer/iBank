package com.deymer.repository.repositories.account

import com.deymer.datasource.remote.account.IAccountDataSource
import com.deymer.datasource.remote.transaction.ITransactionDataSource
import com.deymer.network.manager.INetworkManager
import com.deymer.repository.mappers.toEntity
import com.deymer.repository.mappers.toModel
import com.deymer.repository.models.AccountModel
import com.deymer.repository.models.SimpleAccountModel
import com.deymer.repository.utils.OnResult
import com.deymer.repository.utils.RepositoryConstants.Tags.TAG_NULL
import com.google.firebase.firestore.FirebaseFirestoreException
import java.lang.Exception
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountDataSource: IAccountDataSource,
    private val transactionDataSource: ITransactionDataSource,
    private val accountNetworkManager: INetworkManager
): IAccountRepository {

    override suspend fun createAccount(
        simpleAccount: SimpleAccountModel
    ): OnResult<Boolean> {
        return try {
            val userId = accountNetworkManager.getUserInSession()
            val account = simpleAccount.toEntity(userId)
            return if(accountDataSource.createAccount(account)) {
                val accountId = accountDataSource.getAccount(userId)?.documentId.orEmpty()
                val transactions = simpleAccount.transactions.map { it.toEntity(accountId) }
                OnResult.Success(transactionDataSource.createTransactions(transactions))
            } else {
                OnResult.Success(false)
            }
        } catch (exception: FirebaseFirestoreException) {
            OnResult.Error(exception)
        } catch (exception: Exception) {
            OnResult.Error(exception)
        }
    }

    override suspend fun fetchAccount(): OnResult<AccountModel> {
        return try {
            val userId = accountNetworkManager.getUserInSession()
            return accountDataSource.getAccount(userId)?.let { account ->
                val transactions = transactionDataSource.getTransactions(account.documentId)
                OnResult.Success(account.toModel().copy(
                    transactions = transactions.map { it.toModel() }.reversed()
                ))
            } ?: run {
                OnResult.Error(IllegalArgumentException(TAG_NULL))
            }
        } catch (exception: FirebaseFirestoreException) {
            OnResult.Error(exception)
        } catch (exception: Exception) {
            OnResult.Error(exception)
        }
    }

    override suspend fun updateBalance(
        accountId: String,
        balance: Float
    ): OnResult<Boolean> {
        return try {
            OnResult.Success(accountDataSource.updateAccount(accountId, balance))
        } catch (exception: FirebaseFirestoreException) {
            OnResult.Error(exception)
        } catch (exception: Exception) {
            OnResult.Error(exception)
        }
    }
}