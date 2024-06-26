package com.deymer.ibank.features.register

data class RegisterScreenActions(
    val onPrimaryAction: () -> Unit,
    val onSecondaryAction: () -> Unit,
)