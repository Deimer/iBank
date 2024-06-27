package com.deymer.ibank.features.transaction

data class TransactionDetailsActions(
    val onPrimaryAction: () -> Unit
)
