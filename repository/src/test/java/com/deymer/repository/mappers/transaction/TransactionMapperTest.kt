package com.deymer.repository.mappers.transaction

import com.deymer.database.entities.TransactionEntity
import com.deymer.repository.mappers.toEntity
import com.deymer.repository.mappers.toModel
import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionType
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TransactionMapperTest {

    @Test
    fun `SimpleTransactionModel to TransactionEntity mapping`() {
        val simpleTransactionModel = SimpleTransactionModel(
            destinationAccountId = "dest123",
            amount = 150.0f,
            type = TransactionType.DEPOSIT,
            description = "Test deposit"
        )
        val accountId = "account123"
        val transactionEntity = simpleTransactionModel.toEntity(accountId)
        assertEquals(accountId, transactionEntity.accountId)
        assertEquals(simpleTransactionModel.destinationAccountId, transactionEntity.destinationAccountId)
        assertEquals(simpleTransactionModel.amount, transactionEntity.amount, 0.0f)
        assertEquals(simpleTransactionModel.type.name, transactionEntity.type)
        assertEquals(simpleTransactionModel.description, transactionEntity.description)
    }

    @Test
    fun `TransactionEntity to TransactionModel mapping`() {
        val transactionEntity = TransactionEntity(
            documentId = "trans123",
            accountId = "account123",
            destinationAccountId = "dest123",
            amount = 150.0f,
            type = "DEPOSIT",
            createdAt = 1719096926000,
            description = "Test deposit"
        )
        val transactionModel = transactionEntity.toModel()
        assertEquals(transactionEntity.documentId, transactionModel.id)
        assertEquals(transactionEntity.accountId, transactionModel.accountId)
        assertEquals(transactionEntity.destinationAccountId, transactionModel.destinationAccountId)
        assertEquals(transactionEntity.amount, transactionModel.amount, 0.0f)
        assertEquals(TransactionType.valueOf(transactionEntity.type), transactionModel.type)
        assertEquals(transactionEntity.description, transactionModel.description)
    }

    @Test
    fun `SimpleTransactionModel with default values to TransactionEntity mapping`() {
        val simpleTransactionModel = SimpleTransactionModel(
            amount = 100.0f,
            type = TransactionType.WITHDRAWAL,
            description = "Default transaction"
        )
        val accountId = "account123"
        val transactionEntity = simpleTransactionModel.toEntity(accountId)
        assertEquals(accountId, transactionEntity.accountId)
        assertEquals(simpleTransactionModel.destinationAccountId, transactionEntity.destinationAccountId)
        assertEquals(simpleTransactionModel.amount, transactionEntity.amount, 0.0f)
        assertEquals(simpleTransactionModel.type.name, transactionEntity.type)
        assertEquals(simpleTransactionModel.description, transactionEntity.description)
    }

    @Test
    fun `TransactionEntity with custom values to TransactionModel mapping`() {
        val transactionEntity = TransactionEntity(
            documentId = "customTrans123",
            accountId = "customAccount123",
            destinationAccountId = "customDest123",
            amount = 200.0f,
            type = "WITHDRAWAL",
            createdAt = 1718995200000,
            description = "Custom withdrawal"
        )
        val transactionModel = transactionEntity.toModel()
        assertEquals(transactionEntity.documentId, transactionModel.id)
        assertEquals(transactionEntity.accountId, transactionModel.accountId)
        assertEquals(transactionEntity.destinationAccountId, transactionModel.destinationAccountId)
        assertEquals(transactionEntity.amount, transactionModel.amount, 0.0f)
        assertEquals(TransactionType.valueOf(transactionEntity.type), transactionModel.type)
        assertEquals(transactionEntity.description, transactionModel.description)
    }
}