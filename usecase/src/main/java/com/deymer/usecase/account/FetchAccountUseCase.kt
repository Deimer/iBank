package com.deymer.usecase.account

import com.deymer.repository.repositories.account.IAccountRepository
import javax.inject.Inject

class FetchAccountUseCase @Inject constructor(
    private val accountRepository: IAccountRepository
) {

    suspend fun invoke() =
        accountRepository.fetchAccount()
}