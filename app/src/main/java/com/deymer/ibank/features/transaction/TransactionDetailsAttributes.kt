package com.deymer.ibank.features.transaction

data class TransactionDetailsAttributes(
    val transactionId: String,
    val actions: TransactionDetailsActions
)
