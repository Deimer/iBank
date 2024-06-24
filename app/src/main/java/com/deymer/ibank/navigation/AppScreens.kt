package com.deymer.ibank.navigation

import com.deymer.ibank.navigation.IBankRoute.HOME
import com.deymer.ibank.navigation.IBankRoute.LOGIN
import com.deymer.ibank.navigation.IBankRoute.PROFILE
import com.deymer.ibank.navigation.IBankRoute.REGISTER
import com.deymer.ibank.navigation.IBankRoute.SPLASH
import com.deymer.ibank.navigation.IBankRoute.TRANSACTION_DETAILS

object IBankRoute {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val TRANSACTION_DETAILS = "transactionDetail"
    const val PROFILE = "profile"
}

sealed class AppScreens(val route: String) {
    object SplashScreen: AppScreens(SPLASH)
    object LoginScreen: AppScreens(LOGIN)
    object RegisterScreen: AppScreens(REGISTER)
    object HomeScreen: AppScreens(HOME)
    object TransactionDetailScreen: AppScreens(TRANSACTION_DETAILS)
    object ProfileScreen: AppScreens(PROFILE)
}