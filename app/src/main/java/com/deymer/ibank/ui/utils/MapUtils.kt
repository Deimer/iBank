package com.deymer.ibank.ui.utils

import com.deymer.presentation.R
import com.deymer.repository.models.TransactionType.DEPOSIT
import com.deymer.repository.models.TransactionType.WITHDRAWAL
import com.deymer.repository.models.TransactionType.TRANSFER

fun setIconTransaction(
    transactionType: String
) = when(transactionType) {
    DEPOSIT.name -> R.drawable.ic_deposit
    WITHDRAWAL.name -> R.drawable.ic_withdrawal
    TRANSFER.name -> R.drawable.ic_transfer
    else -> R.drawable.ic_deposit
}

fun transactionWin(
    transactionType: String
) = transactionType == DEPOSIT.name