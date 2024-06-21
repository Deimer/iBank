package com.deymer.repository.repositories.account

import com.deymer.repository.models.AccountModel
import com.deymer.repository.models.SimpleAccountModel
import com.deymer.repository.utils.OnResult

interface IAccountRepository {

    suspend fun createAccount(
        simpleAccount: SimpleAccountModel
    ): OnResult<Boolean>

    suspend fun fetchAccount(): OnResult<AccountModel>

    suspend fun updateBalance(
        accountId: String,
        balance: Float
    ): OnResult<Boolean>
}