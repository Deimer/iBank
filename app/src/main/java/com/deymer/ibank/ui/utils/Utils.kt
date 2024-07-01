package com.deymer.ibank.ui.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.deymer.presentation.R
import com.deymer.repository.models.TransactionType.DEPOSIT
import com.deymer.repository.models.TransactionType.WITHDRAWAL
import com.deymer.repository.models.TransactionType.TRANSFER
import java.io.File

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

fun createImageUri(context: Context): Uri? {
    val timeStamp: String = System.currentTimeMillis().toString()
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val authority = "${context.packageName}.provider"
    return storageDir?.let {
        val fileName = "JPEG_${timeStamp}_"
        val file = File.createTempFile(fileName, ".jpg", it)
        FileProvider.getUriForFile(context, authority, file)
    }
}