package com.deymer.ibank.features.home

data class HomeScreenActions(
    val onPrimaryAction: () -> Unit,
    val onSecondaryAction: (transactionId: String) -> Unit,
)
