package com.deymer.ibank.ui.models

data class UIUserModel(
    val shortName: String = "",
    val fullName: String = "",
    val email: String = "",
    val urlPhoto: String = "",
    val accountNumber: String = "",
    val numberTransactions: Int = 0,
    val createdAt: String = "",
)
