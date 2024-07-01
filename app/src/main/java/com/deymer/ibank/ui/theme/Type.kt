package com.deymer.ibank.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.deymer.ibank.ui.colors.pineappleDark
import com.deymer.ibank.ui.colors.siennaDark
import com.deymer.ibank.ui.colors.seaGreen
import com.deymer.presentation.R

val poppinsFamily = FontFamily(
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
)

// Set of Material typography styles to start with
val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.12.sp,
    ),

    headlineMedium = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = 0.01.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.01.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    bodySmall = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    ),

    labelLarge = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),

    labelMedium = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),

    labelSmall = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
)

val tagTransactionWin = TextStyle(
    fontFamily = poppinsFamily,
    color = seaGreen,
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium
)

val tagTransactionLost = TextStyle(
    fontFamily = poppinsFamily,
    color = pineappleDark,
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium
)