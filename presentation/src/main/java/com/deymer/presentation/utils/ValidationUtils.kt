package com.deymer.presentation.utils

import android.util.Patterns
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

fun String.isValidEmail() =
    this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean {
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{6,}$"
    val passwordMatcher = Regex(passwordPattern)
    return this.isNotEmpty() && passwordMatcher.matches(this)
}

fun String.validateAmount(maxAmount: Float): Boolean {
    if (this.isBlank() || this.replace(",", "").isBlank()) {
        return true
    }
    if (this == "0") {
        return true
    }
    return try {
        this.replace(",", "").toFloat() <= maxAmount
    } catch (e: NumberFormatException) {
        false
    }
}

fun formatMoney(value: Float): String {
    val format = NumberFormat.getNumberInstance(Locale.US)
    format.maximumFractionDigits = 2
    format.isGroupingUsed = value >= 1000f
    return format.format(value)
}

fun parseMoney(text: String): Float {
    val format = NumberFormat.getNumberInstance(Locale.US)
    return try {
        format.parse(text)?.toFloat() ?: 0f
    } catch (e: ParseException) {
        0f
    }
}