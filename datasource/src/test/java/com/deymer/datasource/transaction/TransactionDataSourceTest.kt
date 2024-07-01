package com.deymer.datasource.transaction

import com.deymer.database.DataBaseConstants.Tables.TRANSACTIONS_TABLE
import com.deymer.database.entities.TransactionEntity
import com.deymer.database.managers.firestore.IFirestoreManager
import com.deymer.datasource.DataSourceConstants.KEY_ACCOUNT_ID
import com.deymer.datasource.remote.transaction.TransactionDataSource
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
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TransactionDataSourceTest {

    private lateinit var transactionDataSource: TransactionDataSource
    private val firestoreManager: IFirestoreManager = mock(IFirestoreManager::class.java)

    @Before
    fun setUp() {
        transactionDataSource = TransactionDataSource(firestoreManager)
    }

    @Test
    fun `create transaction should succeed with valid data`() = runTest {
        val transaction = TransactionEntity(
            documentId = "transactionId",
            accountId = "accountId",
            destinationAccountId = "destinationAccountId",
            amount = 100.0f,
            type = "transfer",
            createdAt = 1625247600000,
            description = "Test transaction"
        )
        `when`(firestoreManager.save(transaction)).thenReturn(true)
        val result = transactionDataSource.createTransaction(transaction)
        assertTrue(result)
        verify(firestoreManager).save(transaction)
    }

    @Test
    fun `create transactions should succeed with valid data`() = runTest {
        val transactions = listOf(
            TransactionEntity(
                documentId = "transactionId1",
                accountId = "accountId1",
                destinationAccountId = "destinationAccountId1",
                amount = 200.0f,
                type = "deposit",
                createdAt = 1625247600000,
                description = "Test transaction 1"
            ),
            TransactionEntity(
                documentId = "transactionId2",
                accountId = "accountId2",
                destinationAccountId = "destinationAccountId2",
                amount = 300.0f,
                type = "withdrawal",
                createdAt = 1625247600000,
                description = "Test transaction 2"
            )
        )
        `when`(firestoreManager.save(transactions)).thenReturn(true)
        val result = transactionDataSource.createTransactions(transactions)
        assertTrue(result)
        verify(firestoreManager).save(transactions)
    }

    @Test
    fun `get transaction should return transaction when id is valid`() = runTest {
        val transactionId = "transactionId"
        val transaction = TransactionEntity(
            documentId = "transactionId2",
            accountId = "accountId2",
            destinationAccountId = "destinationAccountId2",
            amount = 300.0f,
            type = "withdrawal",
            createdAt = 1625247600000,
            description = "Test transaction 2"
        )
        val documentSnapshot = mock(DocumentSnapshot::class.java)
        `when`(documentSnapshot.toObject(TransactionEntity::class.java)).thenReturn(transaction)
        `when`(documentSnapshot.id).thenReturn(transactionId)
        `when`(firestoreManager.getById(transactionId, TRANSACTIONS_TABLE)).thenReturn(documentSnapshot)
        val result = transactionDataSource.getTransaction(transactionId)
        assertNotNull(result)
        assertEquals(transactionId, result?.documentId)
        verify(firestoreManager).getById(transactionId, TRANSACTIONS_TABLE)
    }

    @Test
    fun `get transaction should return null when id is invalid`() = runTest {
        val transactionId = "transactionId"
        `when`(firestoreManager.getById(transactionId, TRANSACTIONS_TABLE)).thenReturn(null)
        val result = transactionDataSource.getTransaction(transactionId)
        assertNull(result)
        verify(firestoreManager).getById(transactionId, TRANSACTIONS_TABLE)
    }

    @Test
    fun `get transactions should return list of transactions for valid account id`() = runTest {
        val accountId = "accountId"
        val transaction1 = TransactionEntity(
            documentId = "transactionId1",
            accountId = "accountId",
            destinationAccountId = "destinationAccountId1",
            amount = 200.0f,
            type = "deposit",
            createdAt = 1625247600000,
            description = "Test transaction 1"
        )
        val transaction2 = TransactionEntity(
            documentId = "transactionId2",
            accountId = "accountId",
            destinationAccountId = "destinationAccountId2",
            amount = 300.0f,
            type = "withdrawal",
            createdAt = 1625247600000,
            description = "Test transaction 2"
        )
        val documentSnapshot1 = mock(DocumentSnapshot::class.java)
        val documentSnapshot2 = mock(DocumentSnapshot::class.java)
        `when`(documentSnapshot1.toObject(TransactionEntity::class.java)).thenReturn(transaction1)
        `when`(documentSnapshot1.id).thenReturn("id1")
        `when`(documentSnapshot2.toObject(TransactionEntity::class.java)).thenReturn(transaction2)
        `when`(documentSnapshot2.id).thenReturn("id2")
        `when`(firestoreManager.getListByValue(KEY_ACCOUNT_ID, accountId, TRANSACTIONS_TABLE))
            .thenReturn(listOf(documentSnapshot1, documentSnapshot2))
        val result = transactionDataSource.getTransactions(accountId)
        assertEquals(2, result.size)
        verify(firestoreManager).getListByValue(KEY_ACCOUNT_ID, accountId, TRANSACTIONS_TABLE)
    }

    @Test
    fun `create transaction should fail with invalid data`() = runTest {
        val transaction = TransactionEntity(
            documentId = "transactionId",
            accountId = "accountId",
            destinationAccountId = "destinationAccountId",
            amount = 100.0f,
            type = "transfer",
            createdAt = 1625247600000,
            description = "Test transaction"
        )
        `when`(firestoreManager.save(transaction)).thenReturn(false)
        val result = transactionDataSource.createTransaction(transaction)
        assertFalse(result)
        verify(firestoreManager).save(transaction)
    }

    @Test
    fun `create transactions should fail with invalid data`() = runTest {
        val transactions = listOf(
            TransactionEntity(
                documentId = "transactionId1",
                accountId = "accountId1",
                destinationAccountId = "destinationAccountId1",
                amount = 200.0f,
                type = "deposit",
                createdAt = 1625247600000,
                description = "Test transaction 1"
            ),
            TransactionEntity(
                documentId = "transactionId2",
                accountId = "accountId2",
                destinationAccountId = "destinationAccountId2",
                amount = 300.0f,
                type = "withdrawal",
                createdAt = 1625247600000,
                description = "Test transaction 2"
            )
        )
        `when`(firestoreManager.save(transactions)).thenReturn(false)
        val result = transactionDataSource.createTransactions(transactions)
        assertFalse(result)
        verify(firestoreManager).save(transactions)
    }

    @Test
    fun `get transaction should return null with invalid id`() = runTest {
        val transactionId = "invalidTransactionId"
        `when`(firestoreManager.getById(transactionId, TRANSACTIONS_TABLE)).thenReturn(null)

        val result = transactionDataSource.getTransaction(transactionId)
        assertNull(result)
        verify(firestoreManager).getById(transactionId, TRANSACTIONS_TABLE)
    }

    @Test
    fun `get transactions should return empty list with invalid account id`() = runTest {
        val accountId = "accountId"
        `when`(firestoreManager.getListByValue(KEY_ACCOUNT_ID, accountId, TRANSACTIONS_TABLE))
            .thenReturn(emptyList())

        val result = transactionDataSource.getTransactions(accountId)
        assertTrue(result.isEmpty())
        verify(firestoreManager).getListByValue(KEY_ACCOUNT_ID, accountId, TRANSACTIONS_TABLE)
    }
}