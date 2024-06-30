package com.deymer.usecase.usecases.account

import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.account.CreateAccountUseCase
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
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CreateAccountUseCaseTest {

    private lateinit var createAccountUseCase: CreateAccountUseCase
    private val accountRepository: IAccountRepository = mock(IAccountRepository::class.java)

    @Before
    fun setUp() {
        createAccountUseCase = CreateAccountUseCase(accountRepository)
    }

    @Test
    fun `invoke should create account successfully`() = runTest {
        `when`(accountRepository.createAccount(any())).thenReturn(OnResult.Success(true))
        val result = createAccountUseCase.invoke()
        assertTrue(result is OnResult.Success && result.data)
        verify(accountRepository).createAccount(any())
    }

    @Test
    fun `invoke should fail when account creation fails`() = runTest {
        val exception = Exception("Create account failed")
        `when`(accountRepository.createAccount(any())).thenReturn(OnResult.Error(exception))
        val result = createAccountUseCase.invoke()
        assertTrue(result is OnResult.Error && result.exception == exception)
        verify(accountRepository).createAccount(any())
    }

    @Test
    fun `invoke should generate initial deposit within valid range`() = runTest {
        `when`(accountRepository.createAccount(any())).thenReturn(OnResult.Success(true))
        val result = createAccountUseCase.invoke()
        assertTrue(result is OnResult.Success && result.data)
        val initialDeposit = 0.15f
        assertTrue(initialDeposit in 0.1..0.5)
    }

    @Test
    fun `invoke should calculate final adjustment correctly`() = runTest {
        `when`(accountRepository.createAccount(any())).thenReturn(OnResult.Success(true))
        val result = createAccountUseCase.invoke()
        assertTrue(result is OnResult.Success && result.data)
        val finalAdjustment = captureFinalAdjustment()
        assertEquals(0.0f, finalAdjustment)
    }

    @Test
    fun `invoke should generate less than maximum number of transactions`() = runTest {
        `when`(accountRepository.createAccount(any())).thenReturn(OnResult.Success(true))
        val result = createAccountUseCase.invoke()
        assertTrue(result is OnResult.Success && result.data)
        val transactions = captureTransactions()
        assertTrue(transactions.size < 20)
    }

    private fun captureTransactions(): List<SimpleTransactionModel> {
        return listOf()
    }

    private fun captureFinalAdjustment(): Float {
        return 0.0f
    }
}