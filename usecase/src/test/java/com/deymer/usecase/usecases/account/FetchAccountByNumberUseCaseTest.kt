package com.deymer.usecase.usecases.account

import com.deymer.repository.models.AccountModel
import com.deymer.repository.models.Currency
import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.account.FetchAccountByNumberUseCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FetchAccountByNumberUseCaseTest {

    private lateinit var fetchAccountByNumberUseCase: FetchAccountByNumberUseCase
    private val accountRepository: IAccountRepository = mock(IAccountRepository::class.java)

    @Before
    fun setUp() {
        fetchAccountByNumberUseCase = FetchAccountByNumberUseCase(accountRepository)
    }

    @Test
    fun `invoke should fetch account successfully`() = runTest {
        val accountNumber = "123456789"
        val accountModel = AccountModel(
            id = "accountId",
            userId = "userId",
            number = accountNumber,
            balance = 1000f,
            currency = Currency.USD,
            createdAt = "2024-06-25",
            shortDate = "06-25",
            transactions = emptyList()
        )
        whenever(accountRepository.fetchAccount(accountNumber)).thenReturn(OnResult.Success(accountModel))
        val result = fetchAccountByNumberUseCase.invoke(accountNumber)
        assertTrue(result is OnResult.Success && result.data == accountModel)
        verify(accountRepository).fetchAccount(accountNumber)
    }

    @Test
    fun `invoke should fail with general exception`() = runTest {
        val accountNumber = "123456789"
        val exception = Exception("General error")
        whenever(accountRepository.fetchAccount(accountNumber)).thenReturn(OnResult.Error(exception))
        val result = fetchAccountByNumberUseCase.invoke(accountNumber)
        assertTrue(result is OnResult.Error && result.exception is Exception)
        verify(accountRepository).fetchAccount(accountNumber)
    }

    @Test
    fun `invoke should handle empty account number`() = runTest {
        val accountNumber = ""
        val exception = IllegalArgumentException("Account number cannot be empty")
        whenever(accountRepository.fetchAccount(accountNumber)).thenReturn(OnResult.Error(exception))
        val result = fetchAccountByNumberUseCase.invoke(accountNumber)
        assertTrue(result is OnResult.Error && result.exception is IllegalArgumentException)
        verify(accountRepository).fetchAccount(accountNumber)
    }

    @Test
    fun `invoke should return null for non-existent account`() = runTest {
        val accountNumber = "nonExistentAccount"
        whenever(accountRepository.fetchAccount(accountNumber)).thenReturn(OnResult.Error(IllegalArgumentException("Account not found")))
        val result = fetchAccountByNumberUseCase.invoke(accountNumber)
        assertTrue(result is OnResult.Error && result.exception is IllegalArgumentException)
        verify(accountRepository).fetchAccount(accountNumber)
    }

    @Test
    fun `invoke should return default currency for new account`() = runTest {
        val accountNumber = "newAccountNumber"
        val accountModel = AccountModel(
            id = "accountId",
            userId = "userId",
            number = accountNumber,
            balance = 0f,
            currency = Currency.USD,
            createdAt = "2024-06-25",
            shortDate = "06-25",
            transactions = emptyList()
        )
        whenever(accountRepository.fetchAccount(accountNumber)).thenReturn(OnResult.Success(accountModel))
        val result = fetchAccountByNumberUseCase.invoke(accountNumber)
        assertTrue(result is OnResult.Success && result.data.currency == Currency.USD)
        verify(accountRepository).fetchAccount(accountNumber)
    }

    @Test
    fun `invoke should handle malformed account number`() = runTest {
        val accountNumber = "malformedAccountNumber@#"
        whenever(accountRepository.fetchAccount(accountNumber)).thenReturn(OnResult.Error(IllegalArgumentException("Malformed account number")))
        val result = fetchAccountByNumberUseCase.invoke(accountNumber)
        assertTrue(result is OnResult.Error && result.exception is IllegalArgumentException)
        verify(accountRepository).fetchAccount(accountNumber)
    }
}