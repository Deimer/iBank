package com.deymer.repository.mappers.account

import com.deymer.database.entities.AccountEntity
import com.deymer.repository.mappers.toEntity
import com.deymer.repository.mappers.toModel
import com.deymer.repository.models.Currency
import com.deymer.repository.models.SimpleAccountModel
import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionModel
import com.deymer.repository.models.TransactionType
import junit.framework.TestCase.assertEquals
import org.junit.Test

class AccountMapperTest {

    @Test
    fun `SimpleAccountModel to AccountEntity mapping`() {
        val simpleAccountModel = SimpleAccountModel(
            balance = 100.0f,
            currency = "USD",
            transactions = listOf(
                SimpleTransactionModel(
                    destinationAccountId = "dest123",
                    amount = 50.0f,
                    type = TransactionType.DEPOSIT,
                    description = "Test deposit"
                )
            )
        )
        val userId = "user123"
        val accountEntity = simpleAccountModel.toEntity(userId)
        assertEquals(userId, accountEntity.userId)
        assertEquals(simpleAccountModel.balance, accountEntity.balance, 0.0f)
        assertEquals(simpleAccountModel.currency, accountEntity.currency)
    }

    @Test
    fun `AccountEntity to AccountModel mapping`() {
        val accountEntity = AccountEntity(
            documentId = "account123",
            userId = "user123",
            number = "1234567890",
            balance = 100.0f,
            currency = "USD",
            createdAt = 1719096926000
        )
        val accountModel = accountEntity.toModel()
        assertEquals(accountEntity.documentId, accountModel.id)
        assertEquals(accountEntity.userId, accountModel.userId)
        assertEquals(accountEntity.number, accountModel.number)
        assertEquals(accountEntity.balance, accountModel.balance, 0.0f)
        assertEquals(Currency.valueOf(accountEntity.currency), accountModel.currency)
        assertEquals(toHumanDate(), accountModel.createdAt)
        assertEquals(toShortHumanDate(), accountModel.shortDate)
        assertEquals(emptyList<TransactionModel>(), accountModel.transactions)
    }

    @Test
    fun `AccountEntity with default values to AccountModel mapping`() {
        val accountEntity = AccountEntity(currency = "USD")
        val accountModel = accountEntity.toModel()
        assertEquals(accountEntity.documentId, accountModel.id)
        assertEquals(accountEntity.userId, accountModel.userId)
        assertEquals(accountEntity.number, accountModel.number)
        assertEquals(accountEntity.balance, accountModel.balance, 0.0f)
        assertEquals(Currency.valueOf(accountEntity.currency), accountModel.currency)
        assertEquals("today", accountModel.shortDate)
        assertEquals(emptyList<TransactionModel>(), accountModel.transactions)
    }

    @Test
    fun `AccountEntity with custom values to AccountModel mapping`() {
        val accountEntity = AccountEntity(
            documentId = "customAccount123",
            userId = "customUser123",
            number = "9876543210",
            balance = 500.0f,
            currency = "EUR",
            createdAt = 1719096926000
        )
        val accountModel = accountEntity.toModel()
        assertEquals(accountEntity.documentId, accountModel.id)
        assertEquals(accountEntity.userId, accountModel.userId)
        assertEquals(accountEntity.number, accountModel.number)
        assertEquals(accountEntity.balance, accountModel.balance, 0.0f)
        assertEquals(Currency.valueOf(accountEntity.currency), accountModel.currency)
        assertEquals(toHumanDate(), accountModel.createdAt)
        assertEquals(toShortHumanDate(), accountModel.shortDate)
        assertEquals(emptyList<TransactionModel>(), accountModel.transactions)
    }

    private fun toHumanDate(): String {
        return "22 - junio - 2024, 05:55 p. m."
    }

    private fun toShortHumanDate(): String {
        return "Sábado 22 - junio"
    }
}