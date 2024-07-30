package com.deymer.usecase.account

import com.deymer.repository.models.Currency
import com.deymer.repository.models.SimpleAccountModel
import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.utils.OnResult
import com.deymer.repository.utils.RepositoryConstants.DESCRIPTIONS
import com.deymer.repository.utils.RepositoryConstants.Tags
import javax.inject.Inject
import kotlin.random.Random

class CreateAccountUseCase @Inject constructor(
    private val accountRepository: IAccountRepository
) {

    companion object {
        private const val MIN_TRANSACTIONS = 8
        private const val MAX_TRANSACTIONS = 20
        private const val INITIAL_DEPOSIT_MIN = 1000
        private const val INITIAL_DEPOSIT_MAX = 10000
        private const val INITIAL_DEPOSIT_STEP = 1000
        private const val PERCENTAGE_DIVISOR = 100.0f
        private const val ROUNDING_FACTOR = 100
        private const val MIN_TRANSACTION_AMOUNT = 1
        private const val MAX_TRANSACTION_AMOUNT = 1000
    }

    suspend fun invoke(): OnResult<Boolean> {
        val transactions = generateRandomTransactions()
        val totalAmount = calculateTotalAmount(transactions)
        val simpleAccount = SimpleAccountModel(
            balance = totalAmount,
            currency = Currency.USD.name,
            transactions = transactions
        )
        return accountRepository.createAccount(simpleAccount)
    }

    private fun generateRandomTransactions(): List<SimpleTransactionModel> {
        val random = Random(System.currentTimeMillis())
        val numTransactions = random.nextInt(MIN_TRANSACTIONS, MAX_TRANSACTIONS)
        val initialDeposit = generateInitialDeposit(random)
        val initialTransaction = createTransaction(initialDeposit, TransactionType.DEPOSIT)
        val otherTransactions = (1 until numTransactions).map {
            val type = generateTransactionType(random)
            val amount = generateTransactionAmount(random)
            createTransaction(amount, type)
        }
        return listOf(initialTransaction) + otherTransactions
    }

    private fun generateInitialDeposit(random: Random): Float {
        val possibleValues = (INITIAL_DEPOSIT_MIN..INITIAL_DEPOSIT_MAX step INITIAL_DEPOSIT_STEP).toList()
        return possibleValues[random.nextInt(possibleValues.size)].toFloat()
    }

    private fun generateTransactionAmount(random: Random): Float {
        return ((random.nextInt(MIN_TRANSACTION_AMOUNT, MAX_TRANSACTION_AMOUNT) * ROUNDING_FACTOR).toFloat() / PERCENTAGE_DIVISOR)
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
        return if (random.nextBoolean()) TransactionType.DEPOSIT else TransactionType.WITHDRAWAL
    }

    private fun calculateTotalAmount(transactions: List<SimpleTransactionModel>): Float {
        var totalAmount = 0f
        transactions.forEach { transaction ->
            totalAmount += if (transaction.type == TransactionType.DEPOSIT) {
                transaction.amount
            } else {
                -transaction.amount
            }
        }
        return totalAmount
    }
}