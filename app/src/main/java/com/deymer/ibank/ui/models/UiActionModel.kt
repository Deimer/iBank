package com.deymer.ibank.ui.models

import androidx.annotation.DrawableRes

data class UiActionModel(
    @DrawableRes val icon: Int,
    val contentDescription: String? = null,
    val onClick: () -> Unit
)
