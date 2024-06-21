package com.deymer.repository.mappers

import com.deymer.database.entities.TransactionEntity
import com.deymer.repository.models.SimpleTransactionModel
import com.deymer.repository.models.TransactionModel
import com.deymer.repository.models.TransactionType

fun SimpleTransactionModel.toEntity(accountId: String) = TransactionEntity(
    accountId = accountId,
    destinationAccountId = this.destinationAccountId,
    amount = this.amount,
    type = this.type.name,
    description = this.description
)

fun TransactionEntity.toModel() = TransactionModel(
    id = this.documentId,
    accountId = this.accountId,
    destinationAccountId = this.destinationAccountId,
    amount = this.amount,
    type = TransactionType.valueOf(this.type),
    createdAt = "${this.createdAt}",
    shortDate = "${this.createdAt}",
    description = this.description,
)