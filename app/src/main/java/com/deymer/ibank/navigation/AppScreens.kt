package com.deymer.ibank.navigation

import com.deymer.ibank.navigation.Routes.AUTH_GRAPH
import com.deymer.ibank.navigation.Routes.HOME
import com.deymer.ibank.navigation.Routes.LOGIN
import com.deymer.ibank.navigation.Routes.PROFILE
import com.deymer.ibank.navigation.Routes.RECHARGE
import com.deymer.ibank.navigation.Routes.REGISTER
import com.deymer.ibank.navigation.Routes.SPLASH
import com.deymer.ibank.navigation.Routes.TRANSACTION_DETAILS
import com.deymer.ibank.navigation.Routes.USER_GRAPH

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val TRANSACTION_DETAILS = "transactionDetail"
    const val RECHARGE = "recharge"

    const val AUTH_GRAPH = "auth_graph"
    const val USER_GRAPH = "user_graph"
}

object RouteArguments {
    const val TRANSACTION_ID = "transactionId"
}

sealed class AppScreens(val route: String) {
    object SplashScreen: AppScreens(SPLASH)
    object LoginScreen: AppScreens(LOGIN)
    object RegisterScreen: AppScreens(REGISTER)
    object HomeScreen: AppScreens(HOME)
    object TransactionDetailScreen: AppScreens(TRANSACTION_DETAILS)
    object ProfileScreen: AppScreens(PROFILE)
    object RechargeScreen: AppScreens(RECHARGE)

    //Graphs
    object AuthGraph: AppScreens(AUTH_GRAPH)
    object UserGraph: AppScreens(USER_GRAPH)
}