package com.deymer.usecase.account

import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.utils.OnResult
import java.text.DecimalFormat
import javax.inject.Inject

class TransferAmountUseCase @Inject constructor(
    private val accountRepository: IAccountRepository
) {

    companion object {
        private const val BALANCE_FORMAT = "#.##"
    }

    suspend fun invoke(
        transferAmount: Float,
        accountId: String,
        currentBalance: Float,
        accountIdDestiny: String,
        currentBalanceDestiny: Float
    ): OnResult<Boolean> {
        val decimalFormat = DecimalFormat(BALANCE_FORMAT)
        val newValue = currentBalance - transferAmount
        val newBalance = decimalFormat.format(newValue).toFloat()
        val updateCurrentBalance = accountRepository.updateBalance(
            accountId, newBalance
        )
        return when (updateCurrentBalance) {
            is OnResult.Success -> {
                val newValueDestiny = currentBalanceDestiny + transferAmount
                val newBalanceDestiny = decimalFormat.format(newValueDestiny).toFloat()
                val transferToDestiny = accountRepository.updateBalance(
                    accountIdDestiny, newBalanceDestiny
                )
                when (transferToDestiny) {
                    is OnResult.Success -> OnResult.Success(true)
                    is OnResult.Error -> OnResult.Error(transferToDestiny.exception)
                }
            }
            is OnResult.Error -> OnResult.Error(updateCurrentBalance.exception)
        }
    }
}