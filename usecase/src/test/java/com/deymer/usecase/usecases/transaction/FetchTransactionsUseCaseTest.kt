package com.deymer.usecase.usecases.transaction

import com.deymer.repository.models.TransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.transaction.ITransactionRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.transaction.FetchTransactionsUseCase
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
class FetchTransactionsUseCaseTest {

    private lateinit var fetchTransactionsUseCase: FetchTransactionsUseCase
    private val transactionRepository: ITransactionRepository = mock(ITransactionRepository::class.java)

    @Before
    fun setUp() {
        fetchTransactionsUseCase = FetchTransactionsUseCase(transactionRepository)
    }

    @Test
    fun `invoke should fetch transactions successfully`() = runTest {
        val accountId = "accountId"
        val transactions = listOf(
            TransactionModel(
                id = "transactionId1",
                accountId = accountId,
                destinationAccountId = "destAccountId1",
                amount = 100.0f,
                type = TransactionType.DEPOSIT,
                createdAt = "2024-05-24T00:00:00Z",
                shortDate = "24-05-2024",
                description = "Deposit"
            ),
            TransactionModel(
                id = "transactionId2",
                accountId = accountId,
                destinationAccountId = "destAccountId2",
                amount = 50.0f,
                type = TransactionType.TRANSFER,
                createdAt = "2024-05-25T00:00:00Z",
                shortDate = "25-05-2024",
                description = "Transfer"
            )
        )
        `when`(transactionRepository.fetchTransactions(
            accountId
        )).thenReturn(OnResult.Success(transactions))
        val result = fetchTransactionsUseCase.invoke(accountId)
        assertTrue(result is OnResult.Success && result.data == transactions)
        verify(transactionRepository).fetchTransactions(accountId)
    }

    @Test
    fun `invoke should return empty list when no transactions are found`() = runTest {
        val accountId = "accountId"
        `when`(transactionRepository.fetchTransactions(
            accountId
        )).thenReturn(OnResult.Success(emptyList()))
        val result = fetchTransactionsUseCase.invoke(accountId)
        assertTrue(result is OnResult.Success && result.data.isEmpty())
        verify(transactionRepository).fetchTransactions(accountId)
    }

    @Test
    fun `invoke should return error when fetching transactions fails`() = runTest {
        val accountId = "accountId"
        val exception = Exception("Failed to fetch transactions")
        `when`(transactionRepository.fetchTransactions(
            accountId
        )).thenReturn(OnResult.Error(exception))
        val result = fetchTransactionsUseCase.invoke(accountId)
        assertTrue(result is OnResult.Error && result.exception == exception)
        verify(transactionRepository).fetchTransactions(accountId)
    }

    @Test
    fun `invoke should return transactions in correct order`() = runTest {
        val accountId = "accountId"
        val transactions = listOf(
            TransactionModel(
                id = "transactionId2",
                accountId = accountId,
                destinationAccountId = "destAccountId2",
                amount = 50.0f,
                type = TransactionType.TRANSFER,
                createdAt = "2024-05-26T00:00:00Z",
                shortDate = "26-05-2024",
                description = "Transfer"
            ),
            TransactionModel(
                id = "transactionId1",
                accountId = accountId,
                destinationAccountId = "destAccountId1",
                amount = 100.0f,
                type = TransactionType.DEPOSIT,
                createdAt = "2024-05-27T00:00:00Z",
                shortDate = "27-05-2024",
                description = "Deposit"
            )
        )
        `when`(transactionRepository.fetchTransactions(
            accountId
        )).thenReturn(OnResult.Success(transactions))
        val result = fetchTransactionsUseCase.invoke(accountId)
        assertTrue(result is OnResult.Success)
        val fetchedTransactions = (result as OnResult.Success).data
        assertEquals("transactionId2", fetchedTransactions[0].id)
        assertEquals("transactionId1", fetchedTransactions[1].id)
        verify(transactionRepository).fetchTransactions(accountId)
    }

    @Test
    fun `invoke should return transactions with correct types`() = runTest {
        val accountId = "accountId"
        val transactions = listOf(
            TransactionModel(
                id = "transactionId1",
                accountId = accountId,
                destinationAccountId = "destAccountId1",
                amount = 100.0f,
                type = TransactionType.DEPOSIT,
                createdAt = "2024-05-26T00:00:00Z",
                shortDate = "26-05-2024",
                description = "Deposit"
            ),
            TransactionModel(
                id = "transactionId2",
                accountId = accountId,
                destinationAccountId = "destAccountId2",
                amount = 50.0f,
                type = TransactionType.TRANSFER,
                createdAt = "2024-05-27T00:00:00Z",
                shortDate = "27-05-2024",
                description = "Transfer"
            )
        )
        `when`(transactionRepository.fetchTransactions(
            accountId
        )).thenReturn(OnResult.Success(transactions))
        val result = fetchTransactionsUseCase.invoke(accountId)
        assertTrue(result is OnResult.Success)
        val fetchedTransactions = (result as OnResult.Success).data
        assertEquals(TransactionType.DEPOSIT, fetchedTransactions[0].type)
        assertEquals(TransactionType.TRANSFER, fetchedTransactions[1].type)
        verify(transactionRepository).fetchTransactions(accountId)
    }
}