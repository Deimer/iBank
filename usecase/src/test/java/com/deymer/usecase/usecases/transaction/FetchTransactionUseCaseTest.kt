package com.deymer.usecase.usecases.transaction

import com.deymer.repository.models.TransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.transaction.ITransactionRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.transaction.FetchTransactionUseCase
import junit.framework.TestCase.assertEquals
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
class FetchTransactionUseCaseTest {

    private lateinit var fetchTransactionUseCase: FetchTransactionUseCase
    private val transactionRepository: ITransactionRepository = mock(ITransactionRepository::class.java)

    @Before
    fun setUp() {
        fetchTransactionUseCase = FetchTransactionUseCase(transactionRepository)
    }

    @Test
    fun `invoke should fetch transaction successfully`() = runTest {
        val transactionId = "transactionId"
        val transaction = TransactionModel(
            id = transactionId,
            accountId = "accountId",
            destinationAccountId = "destAccountId",
            amount = 100.0f,
            type = TransactionType.DEPOSIT,
            createdAt = "2024-05-020T00:00:00Z",
            shortDate = "20-05-2024",
            description = "Deposit"
        )
        `when`(transactionRepository.fetchTransaction(
            transactionId
        )).thenReturn(OnResult.Success(transaction))
        val result = fetchTransactionUseCase.invoke(transactionId)
        assertTrue(result is OnResult.Success && result.data == transaction)
        verify(transactionRepository).fetchTransaction(transactionId)
    }

    @Test
    fun `invoke should return error when transaction not found`() = runTest {
        val transactionId = "transactionId"
        `when`(transactionRepository.fetchTransaction(
            transactionId
        )).thenReturn(OnResult.Error(Exception("Transaction not found")))
        val result = fetchTransactionUseCase.invoke(transactionId)
        assertTrue(result is OnResult.Error)
        verify(transactionRepository).fetchTransaction(transactionId)
    }

    @Test
    fun `invoke should return error when fetching transaction fails`() = runTest {
        val transactionId = "transactionId"
        val exception = Exception("Failed to fetch transaction")
        `when`(transactionRepository.fetchTransaction(
            transactionId
        )).thenReturn(OnResult.Error(exception))
        val result = fetchTransactionUseCase.invoke(transactionId)
        assertTrue(result is OnResult.Error && result.exception == exception)
        verify(transactionRepository).fetchTransaction(transactionId)
    }

    @Test
    fun `invoke should return transaction with correct details`() = runTest {
        val transactionId = "transactionId"
        val transaction = TransactionModel(
            id = transactionId,
            accountId = "accountId",
            destinationAccountId = "destAccountId",
            amount = 100.0f,
            type = TransactionType.DEPOSIT,
            createdAt = "2024-05-020T00:00:00Z",
            shortDate = "20-05-2024",
            description = "Deposit"
        )
        `when`(transactionRepository.fetchTransaction(
            transactionId
        )).thenReturn(OnResult.Success(transaction))
        val result = fetchTransactionUseCase.invoke(transactionId)
        assertTrue(result is OnResult.Success)
        val fetchedTransaction = (result as OnResult.Success).data
        assertEquals(transactionId, fetchedTransaction.id)
        assertEquals("accountId", fetchedTransaction.accountId)
        assertEquals("destAccountId", fetchedTransaction.destinationAccountId)
        assertEquals(100.0f, fetchedTransaction.amount)
        assertEquals(TransactionType.DEPOSIT, fetchedTransaction.type)
        verify(transactionRepository).fetchTransaction(transactionId)
    }
}