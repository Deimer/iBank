package com.deymer.usecase.usecases.account

import com.deymer.repository.models.AccountModel
import com.deymer.repository.models.Currency
import com.deymer.repository.models.TransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.account.FetchAccountUseCase
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
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FetchAccountUseCaseTest {

    private lateinit var fetchAccountUseCase: FetchAccountUseCase
    private val accountRepository: IAccountRepository = mock(IAccountRepository::class.java)

    @Before
    fun setUp() {
        fetchAccountUseCase = FetchAccountUseCase(accountRepository)
    }

    @Test
    fun `invoke should fetch account successfully`() = runTest {
        val accountModel = AccountModel(
            id = "accountId",
            userId = "userId",
            number = "123456789",
            balance = 1000f,
            currency = Currency.USD,
            createdAt = "2024-06-25",
            shortDate = "06-25",
            transactions = emptyList()
        )
        `when`(accountRepository.fetchAccount()).thenReturn(OnResult.Success(accountModel))
        val result = fetchAccountUseCase.invoke()
        assertTrue(result is OnResult.Success && result.data == accountModel)
        verify(accountRepository).fetchAccount()
    }

    @Test
    fun `invoke should fail with general exception`() = runTest {
        val exception = Exception("General error")
        whenever(accountRepository.fetchAccount()).thenReturn(OnResult.Error(exception))
        val result = fetchAccountUseCase.invoke()
        assertTrue(result is OnResult.Error && result.exception is Exception)
        verify(accountRepository).fetchAccount()
    }

    @Test
    fun `invoke should handle no account found`() = runTest {
        whenever(accountRepository.fetchAccount()).thenReturn(OnResult.Error(IllegalArgumentException("No account found")))
        val result = fetchAccountUseCase.invoke()
        assertTrue(result is OnResult.Error && result.exception is IllegalArgumentException)
        verify(accountRepository).fetchAccount()
    }

    @Test
    fun `invoke should return account with transactions`() = runTest {
        val transactions = listOf(
            TransactionModel(
                id = "transactionId1",
                accountId = "accountId",
                destinationAccountId = "destinationAccountId1",
                amount = 200.0f,
                type = TransactionType.DEPOSIT,
                createdAt = "2024-06-25",
                shortDate = "06-25",
                description = "Test transaction 1"
            ),
            TransactionModel(
                id = "transactionId2",
                accountId = "accountId",
                destinationAccountId = "destinationAccountId2",
                amount = 300.0f,
                type = TransactionType.WITHDRAWAL,
                createdAt = "2024-06-25",
                shortDate = "06-25",
                description = "Test transaction 2"
            )
        )
        val accountModel = AccountModel(
            id = "accountId",
            userId = "userId",
            number = "123456789",
            balance = 1000f,
            currency = Currency.USD,
            createdAt = "2024-06-25",
            shortDate = "06-25",
            transactions = transactions
        )
        whenever(accountRepository.fetchAccount()).thenReturn(OnResult.Success(accountModel))
        val result = fetchAccountUseCase.invoke()
        assertTrue(result is OnResult.Success && result.data.transactions == transactions)
        verify(accountRepository).fetchAccount()
    }

    @Test
    fun `invoke should fetch account with multiple transactions`() = runTest {
        val transactions = listOf(
            TransactionModel(
                id = "transactionId1",
                accountId = "accountId",
                destinationAccountId = "destinationAccountId1",
                amount = 200.0f,
                type = TransactionType.DEPOSIT,
                createdAt = "2024-06-25",
                shortDate = "06-25",
                description = "Test transaction 1"
            ),
            TransactionModel(
                id = "transactionId2",
                accountId = "accountId",
                destinationAccountId = "destinationAccountId2",
                amount = 300.0f,
                type = TransactionType.WITHDRAWAL,
                createdAt = "2024-06-25",
                shortDate = "06-25",
                description = "Test transaction 2"
            ),
            TransactionModel(
                id = "transactionId3",
                accountId = "accountId",
                destinationAccountId = "destinationAccountId3",
                amount = 500.0f,
                type = TransactionType.TRANSFER,
                createdAt = "2024-06-25",
                shortDate = "06-25",
                description = "Test transaction 3"
            )
        )
        val accountModel = AccountModel(
            id = "accountId",
            userId = "userId",
            number = "123456789",
            balance = 1000f,
            currency = Currency.USD,
            createdAt = "2024-06-25",
            shortDate = "06-25",
            transactions = transactions
        )
        whenever(accountRepository.fetchAccount()).thenReturn(OnResult.Success(accountModel))
        val result = fetchAccountUseCase.invoke()
        assertTrue(result is OnResult.Success && result.data.transactions.size == 3)
        verify(accountRepository).fetchAccount()
    }
}