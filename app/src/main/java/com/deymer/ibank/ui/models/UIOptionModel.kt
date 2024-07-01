package com.deymer.ibank.ui.models

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.deymer.ibank.ui.colors.dark

data class UIOptionModel(
    @DrawableRes val icon: Int,
    val title: String,
    val textColorTitle: Color = dark,
    val buttonText: String,
    val backgroundColor: Color,
    var onClick: () -> Unit = {}
)
