package com.deymer.ibank.features.splash

data class SplashScreenActions(
    val onPrimaryAction: () -> Unit,
    val onSecondaryAction: () -> Unit
)
