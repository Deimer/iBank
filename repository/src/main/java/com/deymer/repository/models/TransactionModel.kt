package com.deymer.repository.models

data class TransactionModel(
    val id: String,
    val accountId: String,
    val destinationAccountId: String,
    val amount: Float,
    val type: TransactionType,
    val createdAt: String,
    val shortDate: String,
    val description: String
)

enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER
}