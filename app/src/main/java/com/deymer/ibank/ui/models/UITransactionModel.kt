package com.deymer.ibank.ui.models

import androidx.annotation.DrawableRes

data class UITransactionModel(
    @DrawableRes val icon: Int = 0,
    val amount: String = "",
    val type: String = "",
    val isWin: Boolean = false,
    val shortDate: String = "",
    val fullDate: String = "",
    val description: String = "",
    var onClick: () -> Unit = {}
)
