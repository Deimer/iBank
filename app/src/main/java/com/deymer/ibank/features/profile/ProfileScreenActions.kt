package com.deymer.ibank.features.profile

data class ProfileScreenActions(
    val onPrimaryAction: () -> Unit,
    val onSecondaryAction: () -> Unit,
)
