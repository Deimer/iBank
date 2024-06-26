package com.deymer.ibank.ui.utils

import android.util.Patterns

fun String.isValidEmail() =
    this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean {
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{6,}$"
    val passwordMatcher = Regex(passwordPattern)
    return this.isNotEmpty() && passwordMatcher.matches(this)
}