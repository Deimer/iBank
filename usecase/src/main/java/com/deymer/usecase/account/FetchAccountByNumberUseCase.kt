package com.deymer.usecase.account

import com.deymer.repository.repositories.account.IAccountRepository
import javax.inject.Inject

class FetchAccountByNumberUseCase @Inject constructor(
    private val accountRepository: IAccountRepository
) {

    suspend fun invoke(accountNumber: String) =
        accountRepository.fetchAccount(accountNumber)
}