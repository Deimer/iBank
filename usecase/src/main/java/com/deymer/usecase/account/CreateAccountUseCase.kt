package com.deymer.usecase.account

import com.deymer.repository.models.Currency
import com.deymer.repository.models.SimpleAccountModel
import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.utils.OnResult
import com.deymer.repository.utils.RepositoryConstants.DESCRIPTIONS
import com.deymer.repository.utils.RepositoryConstants.Tags
import com.deymer.repository.utils.generateRandomBalances
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

class CreateAccountUseCase @Inject constructor(
    private val accountRepository: IAccountRepository
) {

    companion object {
        private const val INITIAL_VALUE = 5
        private const val MAX_VALUE = 21
        private const val MIN_DEPOSIT_PERCENTAGE = 10
        private const val MAX_DEPOSIT_PERCENTAGE = 51
        private const val PERCENTAGE_DIVISOR = 100.0f
        private const val ROUNDING_FACTOR = 100
    }

    suspend fun invoke(): OnResult<Boolean> {
        val totalAmount = generateRandomBalances()
        val simpleAccount = SimpleAccountModel(
            balance = totalAmount,
            currency = Currency.USD.name,
            transactions = generateTransactions(totalAmount)
        )
        return accountRepository.createAccount(simpleAccount)
    }

    private fun generateTransactions(totalAmount: Float): List<SimpleTransactionModel> {
        val random = Random(System.currentTimeMillis())
        val limit = random.nextInt(INITIAL_VALUE, MAX_VALUE)
        val transactions = mutableListOf<SimpleTransactionModel>()
        val initialDeposit = generateInitialDeposit(totalAmount, random)
        transactions.add(createTransaction(initialDeposit, TransactionType.DEPOSIT))
        generateIntermediateTransactions(transactions, totalAmount, random, limit, initialDeposit)
        val finalAdjustment = calculateFinalAdjustment(totalAmount, initialDeposit)
        transactions.add(createTransaction(finalAdjustment, TransactionType.DEPOSIT))
        return transactions
    }

    private fun generateInitialDeposit(totalAmount: Float, random: Random): Float {
        val initialDepositPercentage = random.nextInt(MIN_DEPOSIT_PERCENTAGE, MAX_DEPOSIT_PERCENTAGE) / PERCENTAGE_DIVISOR
        return (totalAmount * initialDepositPercentage * ROUNDING_FACTOR).roundToInt() / PERCENTAGE_DIVISOR
    }

    private fun generateIntermediateTransactions(
        transactions: MutableList<SimpleTransactionModel>,
        totalAmount: Float,
        random: Random,
        limit: Int,
        runningTotal: Float
    ) {
        var currentRunningTotal = runningTotal
        while (transactions.size < limit - 1) {
            val type = generateTransactionType(random)
            val maxAmount = calculateMaxAmount(type, totalAmount, currentRunningTotal, limit, transactions.size)
            val amount = generateAmount(random, maxAmount)
            currentRunningTotal = updateRunningTotal(type, currentRunningTotal, amount)
            if (currentRunningTotal < 0) continue
            transactions.add(createTransaction(amount, type))
        }
    }

    private fun createTransaction(amount: Float, type: TransactionType): SimpleTransactionModel {
        return SimpleTransactionModel(
            destinationAccountId = Tags.TAG_EMPTY,
            amount = amount,
            type = type,
            description = DESCRIPTIONS.random()
        )
    }

    private fun generateTransactionType(random: Random): TransactionType {
        val transactionTypes = listOf(TransactionType.WITHDRAWAL, TransactionType.TRANSFER)
        return if (random.nextBoolean()) TransactionType.DEPOSIT else transactionTypes[random.nextInt(transactionTypes.size)]
    }

    private fun calculateMaxAmount(
        type: TransactionType,
        totalAmount: Float,
        runningTotal: Float,
        limit: Int,
        transactionCount: Int
    ): Float {
        return if (type == TransactionType.DEPOSIT) {
            (totalAmount - runningTotal) / (limit - transactionCount)
        } else {
            runningTotal / (limit - transactionCount)
        }
    }

    private fun generateAmount(random: Random, maxAmount: Float): Float {
        return ((random.nextFloat() * maxAmount) * ROUNDING_FACTOR).roundToInt() / PERCENTAGE_DIVISOR
    }

    private fun updateRunningTotal(type: TransactionType, runningTotal: Float, amount: Float): Float {
        return runningTotal + if (type == TransactionType.DEPOSIT) amount else -amount
    }

    private fun calculateFinalAdjustment(totalAmount: Float, runningTotal: Float): Float {
        return totalAmount - runningTotal
    }
}