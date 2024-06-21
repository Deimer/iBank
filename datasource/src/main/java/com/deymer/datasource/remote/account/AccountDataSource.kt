package com.deymer.datasource.remote.account

import com.deymer.database.DataBaseConstants.Tables.ACCOUNTS_TABLE
import com.deymer.database.entities.AccountEntity
import com.deymer.database.managers.firestore.IFirestoreManager
import com.deymer.datasource.DataSourceConstants.KEY_BALANCE
import com.deymer.datasource.DataSourceConstants.KEY_USER_ID
import javax.inject.Inject

class AccountDataSource @Inject constructor(
    private val firestoreManager: IFirestoreManager
): IAccountDataSource {

    override suspend fun createAccount(
        account: AccountEntity
    ) = firestoreManager.save(entity = account)

    override suspend fun getAccount(
        userId: String
    ): AccountEntity? {
        return firestoreManager.getByValue(
            KEY_USER_ID,
            userId,
            ACCOUNTS_TABLE
        )?.let {
            it.toObject(AccountEntity::class.java)?.apply {
                documentId = it.id
            }
        }
    }

    override suspend fun updateAccount(
        accountId: String,
        balance: Float
    ): Boolean {
        return firestoreManager.updateValueById(
            accountId,
            KEY_BALANCE,
            balance,
            ACCOUNTS_TABLE
        )
    }
}