package com.deymer.usecase.usecases.transaction

import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.transaction.ITransactionRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.transaction.CreateTransactionUseCase
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
class CreateTransactionUseCaseTest {

    private lateinit var createTransactionUseCase: CreateTransactionUseCase
    private val transactionRepository: ITransactionRepository = mock(ITransactionRepository::class.java)

    @Before
    fun setUp() {
        createTransactionUseCase = CreateTransactionUseCase(transactionRepository)
    }

    @Test
    fun `invoke should create transaction successfully`() = runTest {
        val accountId = "accountId"
        val amount = 100.0f
        val type = TransactionType.DEPOSIT.name
        val description = "Test recharge"
        val transaction = SimpleTransactionModel(
            amount = amount,
            type = TransactionType.valueOf(type),
            description = description
        )
        `when`(transactionRepository.createTransaction(
            accountId, transaction)
        ).thenReturn(OnResult.Success(true))
        val result = createTransactionUseCase.invoke(accountId, amount, type, description)
        assertTrue(result is OnResult.Success && result.data)
        verify(transactionRepository).createTransaction(accountId, transaction)
    }

    @Test
    fun `invoke should fail to create transaction`() = runTest {
        val accountId = "accountId"
        val amount = 100.0f
        val type = TransactionType.DEPOSIT.name
        val description = "Test deposit"
        val transaction = SimpleTransactionModel(
            amount = amount,
            type = TransactionType.valueOf(type),
            description = description
        )
        val exception = Exception("Create transaction failed")
        `when`(transactionRepository.createTransaction(
            accountId, transaction)
        ).thenReturn(OnResult.Error(exception))
        val result = createTransactionUseCase.invoke(accountId, amount, type, description)
        assertTrue(result is OnResult.Error && result.exception == exception)
        verify(transactionRepository).createTransaction(accountId, transaction)
    }

    @Test
    fun `invoke should create transaction with default values`() = runTest {
        val accountId = "accountId"
        val amount = 50.0f
        val defaultType = TransactionType.DEPOSIT.name
        val defaultDescription = ""
        val transaction = SimpleTransactionModel(
            amount = amount,
            type = TransactionType.valueOf(defaultType),
            description = defaultDescription
        )
        `when`(transactionRepository.createTransaction(
            accountId, transaction)
        ).thenReturn(OnResult.Success(true))
        val result = createTransactionUseCase.invoke(accountId, amount)
        assertTrue(result is OnResult.Success && result.data)
        verify(transactionRepository).createTransaction(accountId, transaction)
    }

    @Test
    fun `invoke should create withdrawal transaction`() = runTest {
        val accountId = "accountId"
        val amount = 30.0f
        val type = TransactionType.WITHDRAWAL.name
        val description = "Test withdrawal"
        val transaction = SimpleTransactionModel(
            amount = amount,
            type = TransactionType.valueOf(type),
            description = description
        )
        `when`(transactionRepository.createTransaction(
            accountId, transaction)
        ).thenReturn(OnResult.Success(true))
        val result = createTransactionUseCase.invoke(accountId, amount, type, description)
        assertTrue(result is OnResult.Success && result.data)
        verify(transactionRepository).createTransaction(accountId, transaction)
    }

    @Test
    fun `invoke should create transfer transaction`() = runTest {
        val accountId = "accountId"
        val amount = 75.0f
        val type = TransactionType.TRANSFER.name
        val description = "Test transfer"
        val transaction = SimpleTransactionModel(
            amount = amount,
            type = TransactionType.valueOf(type),
            description = description
        )
        `when`(transactionRepository.createTransaction(
            accountId, transaction)
        ).thenReturn(OnResult.Success(true))
        val result = createTransactionUseCase.invoke(accountId, amount, type, description)
        assertTrue(result is OnResult.Success && result.data)
        verify(transactionRepository).createTransaction(accountId, transaction)
    }
}