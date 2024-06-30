package com.deymer.repository.transaction

import com.deymer.database.entities.TransactionEntity
import com.deymer.datasource.remote.transaction.ITransactionDataSource
import com.deymer.repository.mappers.toModel
import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionType
import com.deymer.repository.repositories.transaction.TransactionRepository
import com.deymer.repository.utils.OnResult
import com.google.firebase.firestore.FirebaseFirestoreException
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class TransactionRepositoryTest {

    private lateinit var transactionRepository: TransactionRepository
    private lateinit var transactionDataSource: ITransactionDataSource

    @Before
    fun setup() {
        transactionDataSource = mock(ITransactionDataSource::class.java)
        transactionRepository = TransactionRepository(transactionDataSource)
    }

    @Test
    fun `createTransaction should return success`() = runTest {
        val accountId = "accountId"
        val transaction = SimpleTransactionModel(
            destinationAccountId = "destinationAccountId",
            amount = 100f,
            type = TransactionType.DEPOSIT,
            description = "description"
        )
        whenever(transactionDataSource.createTransaction(any())).thenReturn(true)
        val result = transactionRepository.createTransaction(accountId, transaction)
        assertTrue(result is OnResult.Success && result.data)
        verify(transactionDataSource).createTransaction(any())
    }

    @Test
    fun `createTransaction should return FirebaseFirestoreException error`() = runTest {
        val accountId = "accountId"
        val transaction = SimpleTransactionModel(
            destinationAccountId = "destinationAccountId",
            amount = 100f,
            type = TransactionType.DEPOSIT,
            description = "description"
        )
        val exception = FirebaseFirestoreException("Error", FirebaseFirestoreException.Code.UNKNOWN)
        doAnswer { throw exception }.`when`(transactionDataSource).createTransaction(any())
        val result = transactionRepository.createTransaction(accountId, transaction)
        assertTrue(result is OnResult.Error && result.exception is FirebaseFirestoreException)
        verify(transactionDataSource).createTransaction(any())
    }

    @Test
    fun `fetchTransaction should return success`() = runTest {
        val transactionId = "transactionId"
        val transactionEntity = TransactionEntity(
            documentId = transactionId,
            accountId = "accountId",
            destinationAccountId = "destinationAccountId",
            amount = 100f,
            type = "DEPOSIT",
            createdAt = System.currentTimeMillis(),
            description = "description"
        )
        whenever(transactionDataSource.getTransaction(transactionId)).thenReturn(transactionEntity)
        val result = transactionRepository.fetchTransaction(transactionId)
        assertTrue(result is OnResult.Success && result.data == transactionEntity.toModel())
        verify(transactionDataSource).getTransaction(transactionId)
    }

    @Test
    fun `fetchTransaction should return FirebaseFirestoreException error`() = runTest {
        val transactionId = "transactionId"
        val exception = FirebaseFirestoreException("Error", FirebaseFirestoreException.Code.UNKNOWN)
        doAnswer { throw exception }.`when`(transactionDataSource).getTransaction(transactionId)
        val result = transactionRepository.fetchTransaction(transactionId)
        assertTrue(result is OnResult.Error && result.exception is FirebaseFirestoreException)
        verify(transactionDataSource).getTransaction(transactionId)
    }

    @Test
    fun `fetchTransactions should return success`() = runTest {
        val accountId = "accountId"
        val transactionEntityList = listOf(
            TransactionEntity(
                documentId = "transactionId",
                accountId = accountId,
                destinationAccountId = "destinationAccountId",
                amount = 100f,
                type = "DEPOSIT",
                createdAt = System.currentTimeMillis(),
                description = "description"
            )
        )
        whenever(transactionDataSource.getTransactions(accountId)).thenReturn(transactionEntityList)
        val result = transactionRepository.fetchTransactions(accountId)
        assertTrue(result is OnResult.Success && result.data == transactionEntityList.map { it.toModel() })
        verify(transactionDataSource).getTransactions(accountId)
    }

    @Test
    fun `fetchTransactions should return FirebaseFirestoreException error`() = runTest {
        val accountId = "accountId"
        val exception = FirebaseFirestoreException("Error", FirebaseFirestoreException.Code.UNKNOWN)
        doAnswer { throw exception }.`when`(transactionDataSource).getTransactions(accountId)
        val result = transactionRepository.fetchTransactions(accountId)
        assertTrue(result is OnResult.Error && result.exception is FirebaseFirestoreException)
        verify(transactionDataSource).getTransactions(accountId)
    }

    @Test
    fun `createTransaction should return generic exception error`() = runTest {
        val accountId = "accountId"
        val transaction = SimpleTransactionModel(
            destinationAccountId = "destinationAccountId",
            amount = 100f,
            type = TransactionType.DEPOSIT,
            description = "description"
        )
        val exception = Exception("Generic Error")
        doAnswer { throw exception }.`when`(transactionDataSource).createTransaction(any())
        val result = transactionRepository.createTransaction(accountId, transaction)
        assertTrue(result is OnResult.Error && result.exception is Exception)
        verify(transactionDataSource).createTransaction(any())
    }

    @Test
    fun `fetchTransaction should return error when transaction not found`() = runTest {
        val transactionId = "transactionId"
        whenever(transactionDataSource.getTransaction(transactionId)).thenReturn(null)
        val result = transactionRepository.fetchTransaction(transactionId)
        assertTrue(result is OnResult.Error && result.exception is IllegalArgumentException)
        verify(transactionDataSource).getTransaction(transactionId)
    }

    @Test
    fun `fetchTransaction should return generic exception error`() = runTest {
        val transactionId = "transactionId"
        val exception = Exception("Generic Error")
        doAnswer { throw exception }.`when`(transactionDataSource).getTransaction(transactionId)
        val result = transactionRepository.fetchTransaction(transactionId)
        assertTrue(result is OnResult.Error && result.exception is Exception)
        verify(transactionDataSource).getTransaction(transactionId)
    }

    @Test
    fun `fetchTransactions should return success with empty list`() = runTest {
        val accountId = "accountId"
        whenever(transactionDataSource.getTransactions(accountId)).thenReturn(emptyList())
        val result = transactionRepository.fetchTransactions(accountId)
        assertTrue(result is OnResult.Success && result.data.isEmpty())
        verify(transactionDataSource).getTransactions(accountId)
    }

    @Test
    fun `fetchTransactions should return generic exception error`() = runTest {
        val accountId = "accountId"
        val exception = Exception("Generic Error")
        doAnswer { throw exception }.`when`(transactionDataSource).getTransactions(accountId)
        val result = transactionRepository.fetchTransactions(accountId)
        assertTrue(result is OnResult.Error && result.exception is Exception)
        verify(transactionDataSource).getTransactions(accountId)
    }
}