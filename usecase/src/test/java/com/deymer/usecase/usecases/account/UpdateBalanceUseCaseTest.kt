package com.deymer.usecase.usecases.account

import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.account.UpdateBalanceUseCase
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
class UpdateBalanceUseCaseTest {

    private lateinit var updateBalanceUseCase: UpdateBalanceUseCase
    private val accountRepository: IAccountRepository = mock(IAccountRepository::class.java)

    @Before
    fun setUp() {
        updateBalanceUseCase = UpdateBalanceUseCase(accountRepository)
    }

    @Test
    fun `invoke should update balance successfully`() = runTest {
        val accountId = "accountId"
        val rechargeValue = 100f
        val currentBalance = 500f
        `when`(accountRepository.updateBalance(accountId, 600f)).thenReturn(OnResult.Success(true))
        val result = updateBalanceUseCase.invoke(accountId, rechargeValue, currentBalance)
        assertTrue(result is OnResult.Success && result.data)
        verify(accountRepository).updateBalance(accountId, 600f)
    }

    @Test
    fun `invoke should fail when updating balance fails`() = runTest {
        val accountId = "accountId"
        val rechargeValue = 100f
        val currentBalance = 500f
        val exception = Exception("Update failed")
        `when`(accountRepository.updateBalance(accountId, 600f)).thenReturn(OnResult.Error(exception))
        val result = updateBalanceUseCase.invoke(accountId, rechargeValue, currentBalance)
        assertTrue(result is OnResult.Error && result.exception == exception)
        verify(accountRepository).updateBalance(accountId, 600f)
    }

    @Test
    fun `invoke should handle float rounding correctly`() = runTest {
        val accountId = "accountId"
        val rechargeValue = 100.1234f
        val currentBalance = 500.5678f
        `when`(accountRepository.updateBalance(accountId, 600.69f)).thenReturn(OnResult.Success(true))
        val result = updateBalanceUseCase.invoke(accountId, rechargeValue, currentBalance)
        assertTrue(result is OnResult.Success && result.data)
        verify(accountRepository).updateBalance(accountId, 600.69f)
    }

    @Test
    fun `invoke should handle zero recharge value correctly`() = runTest {
        val accountId = "accountId"
        val rechargeValue = 0f
        val currentBalance = 500f
        `when`(accountRepository.updateBalance(accountId, 500f)).thenReturn(OnResult.Success(true))
        val result = updateBalanceUseCase.invoke(accountId, rechargeValue, currentBalance)
        assertTrue(result is OnResult.Success && result.data)
        verify(accountRepository).updateBalance(accountId, 500f)
    }

    @Test
    fun `invoke should handle negative recharge value correctly`() = runTest {
        val accountId = "accountId"
        val rechargeValue = -50f
        val currentBalance = 500f
        `when`(accountRepository.updateBalance(accountId, 450f)).thenReturn(OnResult.Success(true))
        val result = updateBalanceUseCase.invoke(accountId, rechargeValue, currentBalance)
        assertTrue(result is OnResult.Success && result.data)
        verify(accountRepository).updateBalance(accountId, 450f)
    }
}