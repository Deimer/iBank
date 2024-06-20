package com.deymer.database.entities

import com.deymer.database.DataBaseConstants.DEFAULT_FLOAT
import com.deymer.database.DataBaseConstants.DEFAULT_STRING
import com.deymer.database.DataBaseConstants.LIMIT_ACCOUNT_MAX_NUMBER
import com.deymer.database.DataBaseConstants.LIMIT_ACCOUNT_NUMBER
import com.google.firebase.firestore.Exclude
import java.lang.System.currentTimeMillis
import kotlin.random.Random.Default.nextLong

data class AccountEntity(
    @get:Exclude var documentId: String = DEFAULT_STRING,
    val userId: String = DEFAULT_STRING,
    val number: String = nextLong(LIMIT_ACCOUNT_NUMBER, LIMIT_ACCOUNT_MAX_NUMBER).toString(),
    val balance: Float = DEFAULT_FLOAT,
    val currency: String = DEFAULT_STRING,
    val createdAt: Long = currentTimeMillis()
)
