package com.deymer.repository.models

data class AccountModel(
    val id: String,
    val userId: String,
    val number: String,
    val balance: Float,
    val currency: Currency,
    val createdAt: String,
    val shortDate: String,
    var transactions: List<TransactionModel> = emptyList(),
)

enum class Currency {
    USD,
    EUR
}