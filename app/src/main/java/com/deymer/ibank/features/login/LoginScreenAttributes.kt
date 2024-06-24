package com.deymer.ibank.features.login

data class LoginScreenAttributes(
    val onNavigateToRegister: () -> Unit,
    val onLoginClick: (String, String) -> Unit
)
