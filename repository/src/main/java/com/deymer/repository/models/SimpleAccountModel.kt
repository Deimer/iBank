package com.deymer.repository.models

data class SimpleAccountModel(
    val balance: Float,
    val currency: String,
    val transactions: List<SimpleTransactionModel>
)
