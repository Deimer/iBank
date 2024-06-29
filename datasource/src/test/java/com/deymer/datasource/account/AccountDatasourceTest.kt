package com.deymer.datasource.account

import com.deymer.database.DataBaseConstants.Tables.ACCOUNTS_TABLE
import com.deymer.database.entities.AccountEntity
import com.deymer.database.managers.firestore.IFirestoreManager
import com.deymer.datasource.DataSourceConstants.KEY_BALANCE
import com.deymer.datasource.remote.account.AccountDataSource
import com.google.firebase.firestore.DocumentSnapshot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
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
class AccountDataSourceTest {

    private lateinit var accountDataSource: AccountDataSource
    private val firestoreManager: IFirestoreManager = mock(IFirestoreManager::class.java)

    @Before
    fun setup() {
        accountDataSource = AccountDataSource(firestoreManager)
    }

    @Test
    fun `create account should succeed with valid data`() = runTest {
        val account = AccountEntity(
            documentId = "accountId",
            userId = "userId",
            number = "123456789",
            balance = 1000.0f,
            currency = "USD",
            createdAt = 1625247600000
        )
        `when`(firestoreManager.save(account)).thenReturn(true)
        val result = accountDataSource.createAccount(account)
        assertTrue(result)
        verify(firestoreManager).save(account)
    }

    @Test
    fun `create account should fail with invalid data`() = runTest {
        val account = AccountEntity(
            documentId = "accountId",
            userId = "userId",
            number = "123456789",
            balance = 1000.0f,
            currency = "USD",
            createdAt = 1625247600000
        )
        `when`(firestoreManager.save(account)).thenReturn(false)
        val result = accountDataSource.createAccount(account)
        assertFalse(result)
        verify(firestoreManager).save(account)
    }

    @Test
    fun `get account should return account when key and value are valid`() = runTest {
        val key = "userId"
        val value = "userIdValue"
        val account = AccountEntity(
            documentId = "accountId",
            userId = "userIdValue",
            number = "123456789",
            balance = 1000.0f,
            currency = "USD",
            createdAt = 1625247600000
        )
        val documentSnapshot = mock(DocumentSnapshot::class.java)
        `when`(documentSnapshot.toObject(AccountEntity::class.java)).thenReturn(account)
        `when`(documentSnapshot.id).thenReturn("accountId")
        `when`(firestoreManager.getByValue(key, value, ACCOUNTS_TABLE)).thenReturn(documentSnapshot)
        val result = accountDataSource.getAccount(key, value)
        assertNotNull(result)
        assertEquals("accountId", result?.documentId)
        verify(firestoreManager).getByValue(key, value, ACCOUNTS_TABLE)
    }

    @Test
    fun `get account should return null when key and value are invalid`() = runTest {
        val key = "userId"
        val value = "invalidUserIdValue"
        `when`(firestoreManager.getByValue(key, value, ACCOUNTS_TABLE)).thenReturn(null)
        val result = accountDataSource.getAccount(key, value)
        assertNull(result)
        verify(firestoreManager).getByValue(key, value, ACCOUNTS_TABLE)
    }

    @Test
    fun `update account balance should succeed with valid data`() = runTest {
        val accountId = "accountId"
        val newBalance = 2000.0f
        `when`(firestoreManager.updateValueById(accountId, KEY_BALANCE, newBalance, ACCOUNTS_TABLE)).thenReturn(true)
        val result = accountDataSource.updateAccount(accountId, newBalance)
        assertTrue(result)
        verify(firestoreManager).updateValueById(accountId, KEY_BALANCE, newBalance, ACCOUNTS_TABLE)
    }

    @Test
    fun `update account balance should fail with invalid data`() = runTest {
        val accountId = "accountId"
        val newBalance = 2000.0f
        `when`(firestoreManager.updateValueById(accountId, KEY_BALANCE, newBalance, ACCOUNTS_TABLE)).thenReturn(false)
        val result = accountDataSource.updateAccount(accountId, newBalance)
        assertFalse(result)
        verify(firestoreManager).updateValueById(accountId, KEY_BALANCE, newBalance, ACCOUNTS_TABLE)
    }

    @Test
    fun `create account with existing number should fail`() = runTest {
        val account = AccountEntity(
            documentId = "accountId",
            userId = "userId",
            number = "123456789",
            balance = 1000.0f,
            currency = "USD",
            createdAt = 1625247600000
        )
        `when`(firestoreManager.save(account)).thenReturn(false)
        val result = accountDataSource.createAccount(account)
        assertFalse(result)
        verify(firestoreManager).save(account)
    }

    @Test
    fun `get account by non-existing key should return null`() = runTest {
        val key = "nonExistingKey"
        val value = "nonExistingValue"
        `when`(firestoreManager.getByValue(key, value, ACCOUNTS_TABLE)).thenReturn(null)
        val result = accountDataSource.getAccount(key, value)
        assertNull(result)
        verify(firestoreManager).getByValue(key, value, ACCOUNTS_TABLE)
    }

    @Test
    fun `update account balance with non-existing account should fail`() = runTest {
        val accountId = "nonExistingAccountId"
        val newBalance = 2000.0f
        `when`(firestoreManager.updateValueById(accountId, KEY_BALANCE, newBalance, ACCOUNTS_TABLE)).thenReturn(false)
        val result = accountDataSource.updateAccount(accountId, newBalance)
        assertFalse(result)
        verify(firestoreManager).updateValueById(accountId, KEY_BALANCE, newBalance, ACCOUNTS_TABLE)
    }

    @Test
    fun `get account should return null when document is null`() = runTest {
        val key = "userId"
        val value = "userIdValue"
        `when`(firestoreManager.getByValue(key, value, ACCOUNTS_TABLE)).thenReturn(null)
        val result = accountDataSource.getAccount(key, value)
        assertNull(result)
        verify(firestoreManager).getByValue(key, value, ACCOUNTS_TABLE)
    }

    @Test
    fun `update account should succeed with valid account and balance`() = runTest {
        val accountId = "accountId"
        val newBalance = 1500.0f
        `when`(firestoreManager.updateValueById(accountId, KEY_BALANCE, newBalance, ACCOUNTS_TABLE)).thenReturn(true)
        val result = accountDataSource.updateAccount(accountId, newBalance)
        assertTrue(result)
        verify(firestoreManager).updateValueById(accountId, KEY_BALANCE, newBalance, ACCOUNTS_TABLE)
    }
}