package com.deymer.repository.account

import com.deymer.database.entities.AccountEntity
import com.deymer.database.entities.TransactionEntity
import com.deymer.datasource.DataSourceConstants.KEY_ACCOUNT_NUMBER
import com.deymer.datasource.DataSourceConstants.KEY_USER_ID
import com.deymer.datasource.remote.account.IAccountDataSource
import com.deymer.datasource.remote.transaction.ITransactionDataSource
import com.deymer.network.manager.INetworkManager
import com.deymer.repository.models.SimpleAccountModel
import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.account.AccountRepository
import com.deymer.repository.utils.OnResult
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AccountRepositoryTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var accountDataSource: IAccountDataSource
    private lateinit var transactionDataSource: ITransactionDataSource
    private lateinit var accountNetworkManager: INetworkManager

    @Before
    fun setUp() {
        accountDataSource = mock(IAccountDataSource::class.java)
        transactionDataSource = mock(ITransactionDataSource::class.java)
        accountNetworkManager = mock(INetworkManager::class.java)
        accountRepository = AccountRepository(accountDataSource, transactionDataSource, accountNetworkManager)
    }

    @Test
    fun `create account should succeed`() = runTest {
        val simpleAccount = SimpleAccountModel(
            balance = 1000.0f,
            currency = "USD",
            transactions = listOf(
                SimpleTransactionModel(
                    destinationAccountId = "destAccountId",
                    amount = 500.0f,
                    type = TransactionType.DEPOSIT,
                    description = "Deposit test"
                )
            )
        )
        val userId = "userId"
        val accountEntity = AccountEntity(userId = userId, balance = 1000.0f, currency = "USD")
        `when`(accountNetworkManager.getUserInSession()).thenReturn(userId)
        `when`(accountDataSource.createAccount(any())).thenReturn(true)
        `when`(accountDataSource.getAccount(KEY_USER_ID, userId)).thenReturn(accountEntity)
        `when`(transactionDataSource.createTransactions(any())).thenReturn(true)
        val result = accountRepository.createAccount(simpleAccount)
        assertTrue(result is OnResult.Success && result.data)
        verify(accountNetworkManager).getUserInSession()
        verify(accountDataSource).createAccount(any())
        verify(accountDataSource).getAccount(KEY_USER_ID, userId)
        verify(transactionDataSource).createTransactions(any())
    }

    @Test
    fun `fetch account should return account model`() = runTest {
        val userId = "userId"
        val accountEntity = AccountEntity(
            userId = userId,
            balance = 1000.0f,
            currency = "USD"
        )
        val transactionEntities = listOf(
            TransactionEntity(
                accountId = "accountId",
                amount = 500.0f,
                type = "DEPOSIT",
                createdAt = 1625247600000,
                description = "Deposit test"
            )
        )
        `when`(accountNetworkManager.getUserInSession()).thenReturn(userId)
        `when`(accountDataSource.getAccount(KEY_USER_ID, userId)).thenReturn(accountEntity)
        `when`(transactionDataSource.getTransactions(any())).thenReturn(transactionEntities)
        val result = accountRepository.fetchAccount()
        assertTrue(result is OnResult.Success)
        verify(accountNetworkManager).getUserInSession()
        verify(accountDataSource).getAccount(KEY_USER_ID, userId)
        verify(transactionDataSource).getTransactions(any())
    }

    @Test
    fun `update balance should succeed`() = runTest {
        val accountId = "accountId"
        val newBalance = 2000f
        `when`(accountDataSource.updateAccount(accountId, newBalance)).thenReturn(true)
        val result = accountRepository.updateBalance(accountId, newBalance)
        assertTrue(result is OnResult.Success && result.data)
        verify(accountDataSource).updateAccount(accountId, newBalance)
    }

    @Test
    fun `fetch account with account number should return account model`() = runTest {
        val accountNumber = "accountNumber"
        val accountEntity = AccountEntity(
            userId = "userId",
            number = accountNumber,
            balance = 1000.0f,
            currency = "USD"
        )
        val transactionEntities = listOf(
            TransactionEntity(
                accountId = "accountId",
                amount = 500.0f,
                type = "DEPOSIT",
                createdAt = 1625247600000,
                description = "Deposit test"
            )
        )
        `when`(accountDataSource.getAccount(KEY_ACCOUNT_NUMBER, accountNumber)).thenReturn(accountEntity)
        `when`(transactionDataSource.getTransactions(any())).thenReturn(transactionEntities)
        val result = accountRepository.fetchAccount(accountNumber)
        assertTrue(result is OnResult.Success)
        verify(accountDataSource).getAccount(KEY_ACCOUNT_NUMBER, accountNumber)
        verify(transactionDataSource).getTransactions(any())
    }

    @Test
    fun `create account should fail with RuntimeException`() = runTest {
        val simpleAccount = SimpleAccountModel(
            balance = 1000.0f,
            currency = "USD",
            transactions = listOf(
                SimpleTransactionModel(
                    destinationAccountId = "destAccountId",
                    amount = 500.0f,
                    type = TransactionType.DEPOSIT,
                    description = "Deposit test"
                )
            )
        )
        `when`(accountNetworkManager.getUserInSession()).thenThrow(RuntimeException("Error"))
        val result = accountRepository.createAccount(simpleAccount)
        assertTrue(result is OnResult.Error)
        verify(accountNetworkManager).getUserInSession()
    }

    @Test
    fun `fetch account should fail with RuntimeException`() = runTest {
        `when`(accountNetworkManager.getUserInSession()).thenThrow(RuntimeException("Error"))
        val result = accountRepository.fetchAccount()
        assertTrue(result is OnResult.Error)
        verify(accountNetworkManager).getUserInSession()
    }

    @Test
    fun `update balance should fail with RuntimeException`() = runTest {
        val accountId = "accountId"
        val newBalance = 1500.0f
        `when`(accountDataSource.updateAccount(accountId, newBalance)).thenThrow(RuntimeException("Error"))
        val result = accountRepository.updateBalance(accountId, newBalance)
        assertTrue(result is OnResult.Error)
        verify(accountDataSource).updateAccount(accountId, newBalance)
    }

    @Test
    fun `fetch account should return account with no transactions`() = runTest {
        val userId = "userId"
        val accountEntity = AccountEntity(
            userId = userId,
            balance = 1000.0f,
            currency = "USD"
        )
        `when`(accountNetworkManager.getUserInSession()).thenReturn(userId)
        `when`(accountDataSource.getAccount(KEY_USER_ID, userId)).thenReturn(accountEntity)
        `when`(transactionDataSource.getTransactions(anyString())).thenReturn(emptyList())
        val result = accountRepository.fetchAccount()
        assertTrue(result is OnResult.Success)
        assertEquals(0, (result as OnResult.Success).data.transactions.size)
        verify(accountNetworkManager).getUserInSession()
        verify(accountDataSource).getAccount(KEY_USER_ID, userId)
        verify(transactionDataSource).getTransactions(anyString())
    }

    @Test
    fun `create account should succeed with no transactions`() = runTest {
        val simpleAccount = SimpleAccountModel(
            balance = 1000.0f,
            currency = "USD",
            transactions = emptyList()
        )
        val userId = "userId"
        val accountEntity = AccountEntity(userId = userId, balance = 1000.0f, currency = "USD")
        `when`(accountNetworkManager.getUserInSession()).thenReturn(userId)
        `when`(accountDataSource.createAccount(anyOrNull())).thenReturn(true)
        `when`(accountDataSource.getAccount(KEY_USER_ID, userId)).thenReturn(accountEntity)
        `when`(transactionDataSource.createTransactions(anyOrNull())).thenReturn(true)
        val result = accountRepository.createAccount(simpleAccount)
        assertTrue(result is OnResult.Success && result.data)
        verify(accountNetworkManager).getUserInSession()
        verify(accountDataSource).createAccount(anyOrNull())
        verify(accountDataSource).getAccount(KEY_USER_ID, userId)
        verify(transactionDataSource).createTransactions(emptyList())
    }

    @Test
    fun `update balance should update correctly`() = runTest {
        val accountId = "accountId"
        val newBalance = 2000.0f
        `when`(accountDataSource.updateAccount(accountId, newBalance)).thenReturn(true)
        val result = accountRepository.updateBalance(accountId, newBalance)
        assertTrue(result is OnResult.Success && result.data)
        verify(accountDataSource).updateAccount(accountId, newBalance)
    }

    @Test
    fun `fetch account with account number should return account with no transactions`() = runTest {
        val accountNumber = "accountNumber"
        val accountEntity = AccountEntity(
            userId = "userId",
            number = accountNumber,
            balance = 1000.0f,
            currency = "USD"
        )
        `when`(accountDataSource.getAccount(KEY_ACCOUNT_NUMBER, accountNumber)).thenReturn(accountEntity)
        `when`(transactionDataSource.getTransactions(anyString())).thenReturn(emptyList())
        val result = accountRepository.fetchAccount(accountNumber)
        assertTrue(result is OnResult.Success)
        assertEquals(0, (result as OnResult.Success).data.transactions.size)
        verify(accountDataSource).getAccount(KEY_ACCOUNT_NUMBER, accountNumber)
        verify(transactionDataSource).getTransactions(anyString())
    }
}