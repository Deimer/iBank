package com.deymer.database.entities

import com.deymer.database.DataBaseConstants.DEFAULT_FLOAT
import com.deymer.database.DataBaseConstants.DEFAULT_STRING
import com.google.firebase.firestore.Exclude
import java.lang.System.currentTimeMillis

data class TransactionEntity(
    @get:Exclude var documentId: String = DEFAULT_STRING,
    val accountId: String = DEFAULT_STRING,
    val destinationAccountId: String = DEFAULT_STRING,
    val amount: Float = DEFAULT_FLOAT,
    val type: String = DEFAULT_STRING,
    val createdAt: Long = currentTimeMillis(),
    val description: String = DEFAULT_STRING
)
