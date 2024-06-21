package com.deymer.repository.models

data class SimpleTransactionModel(
    val destinationAccountId: String = "",
    val amount: Float,
    val type: TransactionType,
    val description: String
)
