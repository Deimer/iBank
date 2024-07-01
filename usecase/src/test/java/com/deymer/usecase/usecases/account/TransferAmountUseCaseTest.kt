package com.deymer.usecase.usecases.account

import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.account.TransferAmountUseCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TransferAmountUseCaseTest {

    private lateinit var transferAmountUseCase: TransferAmountUseCase
    private val accountRepository: IAccountRepository = mock(IAccountRepository::class.java)

    @Before
    fun setUp() {
        transferAmountUseCase = TransferAmountUseCase(accountRepository)
    }

    @Test
    fun `invoke should transfer amount successfully`() = runTest {
        val transferAmount = 100f
        val accountId = "accountId"
        val currentBalance = 500f
        val accountIdDestiny = "accountIdDestiny"
        val currentBalanceDestiny = 300f
        `when`(accountRepository.updateBalance(accountId, 400f)).thenReturn(OnResult.Success(true))
        `when`(accountRepository.updateBalance(accountIdDestiny, 400f)).thenReturn(OnResult.Success(true))
        val result = transferAmountUseCase.invoke(
            transferAmount, accountId, currentBalance, accountIdDestiny, currentBalanceDestiny
        )
        assertTrue(result is OnResult.Success && result.data)
        verify(accountRepository).updateBalance(accountId, 400f)
        verify(accountRepository).updateBalance(accountIdDestiny, 400f)
    }

    @Test
    fun `invoke should fail when updating current account balance fails`() = runTest {
        val transferAmount = 100.0f
        val accountId = "accountId"
        val currentBalance = 1000.0f
        val accountIdDestiny = "accountIdDestiny"
        val currentBalanceDestiny = 500.0f
        val exception = IllegalArgumentException("Update balance failed")
        whenever(accountRepository.updateBalance(eq(accountId), any())).thenReturn(OnResult.Error(exception))
        val result = transferAmountUseCase.invoke(
            transferAmount = transferAmount,
            accountId = accountId,
            currentBalance = currentBalance,
            accountIdDestiny = accountIdDestiny,
            currentBalanceDestiny = currentBalanceDestiny,
        )
        assertTrue(result is OnResult.Error && result.exception == exception)
        verify(accountRepository).updateBalance(eq(accountId), eq(900.0f))
        verify(accountRepository, never()).updateBalance(eq(accountIdDestiny), any())
    }

    @Test
    fun `invoke should fail when updating destiny account balance fails`() = runTest {
        val transferAmount = 100f
        val accountId = "accountId"
        val currentBalance = 500f
        val accountIdDestiny = "accountIdDestiny"
        val currentBalanceDestiny = 300f
        val exception = Exception("Update failed")
        `when`(accountRepository.updateBalance(accountId, 400f)).thenReturn(OnResult.Success(true))
        `when`(accountRepository.updateBalance(accountIdDestiny, 400f)).thenReturn(OnResult.Error(exception))
        val result = transferAmountUseCase.invoke(
            transferAmount = transferAmount,
            accountId = accountId,
            currentBalance = currentBalance,
            accountIdDestiny = accountIdDestiny,
            currentBalanceDestiny = currentBalanceDestiny,
        )
        assertTrue(result is OnResult.Error && result.exception == exception)
        verify(accountRepository).updateBalance(accountId, 400f)
        verify(accountRepository).updateBalance(accountIdDestiny, 400f)
    }

    @Test
    fun `invoke should handle float rounding correctly`() = runTest {
        val transferAmount = 100.1234f
        val accountId = "accountId"
        val currentBalance = 500.5678f
        val accountIdDestiny = "accountIdDestiny"
        val currentBalanceDestiny = 300.4321f
        `when`(accountRepository.updateBalance(accountId, 400.44f)).thenReturn(OnResult.Success(true))
        `when`(accountRepository.updateBalance(accountIdDestiny, 400.56f)).thenReturn(OnResult.Success(true))
        val result = transferAmountUseCase.invoke(
            transferAmount = transferAmount,
            accountId = accountId,
            currentBalance = currentBalance,
            accountIdDestiny = accountIdDestiny,
            currentBalanceDestiny = currentBalanceDestiny,
        )
        assertTrue(result is OnResult.Success && result.data)
        verify(accountRepository).updateBalance(accountId, 400.44f)
        verify(accountRepository).updateBalance(accountIdDestiny, 400.56f)
    }

    @Test
    fun `invoke should handle zero transfer amount correctly`() = runTest {
        val transferAmount = 0f
        val accountId = "accountId"
        val currentBalance = 500f
        val accountIdDestiny = "accountIdDestiny"
        val currentBalanceDestiny = 300f
        `when`(accountRepository.updateBalance(accountId, 500f)).thenReturn(OnResult.Success(true))
        `when`(accountRepository.updateBalance(accountIdDestiny, 300f)).thenReturn(OnResult.Success(true))
        val result = transferAmountUseCase.invoke(
            transferAmount = transferAmount,
            accountId = accountId,
            currentBalance = currentBalance,
            accountIdDestiny = accountIdDestiny,
            currentBalanceDestiny = currentBalanceDestiny,
        )
        assertTrue(result is OnResult.Success && result.data)
        verify(accountRepository).updateBalance(accountId, 500f)
        verify(accountRepository).updateBalance(accountIdDestiny, 300f)
    }
}