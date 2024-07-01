package com.deymer.ibank.features.login

data class LoginScreenActions(
    val onPrimaryAction: () -> Unit,
    val onSecondaryAction: () -> Unit,
)
