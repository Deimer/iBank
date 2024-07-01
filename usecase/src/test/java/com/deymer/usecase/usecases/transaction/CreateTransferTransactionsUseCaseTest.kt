package com.deymer.usecase.usecases.transaction

import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.transaction.ITransactionRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.transaction.CreateTransferTransactionsUseCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CreateTransferTransactionsUseCaseTest {

    private lateinit var createTransferTransactionsUseCase: CreateTransferTransactionsUseCase
    private val transactionRepository: ITransactionRepository = mock(ITransactionRepository::class.java)

    @Before
    fun setUp() {
        createTransferTransactionsUseCase = CreateTransferTransactionsUseCase(transactionRepository)
    }

    @Test
    fun `invoke should create transfer transactions successfully`() = runTest {
        val accountId = "accountId"
        val accountIdDestiny = "accountIdDestiny"
        val amount = 100.0f
        val description = "Transfer description"
        val transactionOrigin = SimpleTransactionModel(
            destinationAccountId = accountIdDestiny,
            amount = amount,
            type = TransactionType.TRANSFER,
            description = description
        )
        val transactionDestiny = SimpleTransactionModel(
            destinationAccountId = accountId,
            amount = amount,
            type = TransactionType.DEPOSIT,
            description = description
        )
        `when`(transactionRepository.createTransaction(accountId, transactionOrigin))
            .thenReturn(OnResult.Success(true))
        `when`(transactionRepository.createTransaction(accountIdDestiny, transactionDestiny))
            .thenReturn(OnResult.Success(true))
        val result = createTransferTransactionsUseCase.invoke(accountId, accountIdDestiny, amount, description)
        assertTrue(result is OnResult.Success && result.data)
        verify(transactionRepository).createTransaction(accountId, transactionOrigin)
        verify(transactionRepository).createTransaction(accountIdDestiny, transactionDestiny)
    }

    @Test
    fun `invoke should create transfer transactions with default description`() = runTest {
        val accountId = "accountId"
        val accountIdDestiny = "accountIdDestiny"
        val amount = 50.0f
        val defaultDescription = ""
        val transactionOrigin = SimpleTransactionModel(
            destinationAccountId = accountIdDestiny,
            amount = amount,
            type = TransactionType.TRANSFER,
            description = defaultDescription
        )
        val transactionDestiny = SimpleTransactionModel(
            destinationAccountId = accountId,
            amount = amount,
            type = TransactionType.DEPOSIT,
            description = defaultDescription
        )
        `when`(transactionRepository.createTransaction(accountId, transactionOrigin))
            .thenReturn(OnResult.Success(true))
        `when`(transactionRepository.createTransaction(
            accountIdDestiny, transactionDestiny)
        ).thenReturn(OnResult.Success(true))
        val result = createTransferTransactionsUseCase.invoke(accountId, accountIdDestiny, amount, defaultDescription)
        assertTrue(result is OnResult.Success && result.data)
        verify(transactionRepository).createTransaction(accountId, transactionOrigin)
        verify(transactionRepository).createTransaction(accountIdDestiny, transactionDestiny)
    }

    @Test
    fun `invoke should fail with generic error when both transactions fail`() = runTest {
        val accountId = "accountId"
        val accountIdDestiny = "accountIdDestiny"
        val amount = 100.0f
        val description = "Transfer description"
        val transactionOrigin = SimpleTransactionModel(
            destinationAccountId = accountIdDestiny,
            amount = amount,
            type = TransactionType.TRANSFER,
            description = description
        )
        val transactionDestiny = SimpleTransactionModel(
            destinationAccountId = accountId,
            amount = amount,
            type = TransactionType.DEPOSIT,
            description = description
        )
        val exceptionOrigin = Exception("Origin transaction failed")
        val exceptionDestiny = Exception("Destiny transaction failed")
        `when`(transactionRepository.createTransaction(accountId, transactionOrigin))
            .thenReturn(OnResult.Error(exceptionOrigin))
        `when`(transactionRepository.createTransaction(
            accountIdDestiny, transactionDestiny)
        ).thenReturn(OnResult.Error(exceptionDestiny))
        val result = createTransferTransactionsUseCase.invoke(accountId, accountIdDestiny, amount, description)
        assertTrue(result is OnResult.Error)
        verify(transactionRepository).createTransaction(accountId, transactionOrigin)
        verify(transactionRepository).createTransaction(accountIdDestiny, transactionDestiny)
    }
}