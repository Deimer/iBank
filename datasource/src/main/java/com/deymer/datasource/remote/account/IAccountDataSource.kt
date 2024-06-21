package com.deymer.datasource.remote.account

import com.deymer.database.entities.AccountEntity

interface IAccountDataSource {

    suspend fun createAccount(
        account: AccountEntity
    ): Boolean

    suspend fun getAccount(
        userId: String
    ): AccountEntity?

    suspend fun updateAccount(
        accountId: String,
        balance: Float
    ): Boolean
}