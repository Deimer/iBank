package com.deymer.usecase.account

import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.utils.OnResult
import java.text.DecimalFormat
import javax.inject.Inject

class UpdateBalanceUseCase @Inject constructor(
    private val accountRepository: IAccountRepository
) {

    companion object {
        private const val BALANCE_FORMAT = "#.##"
    }

    suspend fun invoke(
        accountId: String, rechargeValue: Float, currentBalance: Float
    ): OnResult<Boolean> {
        val newValue = currentBalance + rechargeValue
        val decimalFormat = DecimalFormat(BALANCE_FORMAT)
        val newBalance = decimalFormat.format(newValue).toFloat()
        return accountRepository.updateBalance(
            accountId, newBalance
        )
    }
}