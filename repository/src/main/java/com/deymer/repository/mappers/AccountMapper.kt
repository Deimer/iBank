package com.deymer.repository.mappers

import com.deymer.database.entities.AccountEntity
import com.deymer.repository.models.AccountModel
import com.deymer.repository.models.Currency
import com.deymer.repository.models.SimpleAccountModel
import com.deymer.repository.utils.toHumanDate
import com.deymer.repository.utils.toShortHumanDate

fun SimpleAccountModel.toEntity(userId: String) = AccountEntity(
    userId = userId,
    balance = this.balance,
    currency = this.currency
)

fun AccountEntity.toModel() = AccountModel(
    id = this.documentId,
    userId = this.userId,
    number = this.number,
    balance = this.balance,
    currency = Currency.valueOf(this.currency),
    createdAt = this.createdAt.toHumanDate(),
    shortDate = this.createdAt.toShortHumanDate()
)