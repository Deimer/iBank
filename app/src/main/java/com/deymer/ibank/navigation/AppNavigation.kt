package com.deymer.ibank.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deymer.ibank.features.login.LoginScreen
import com.deymer.ibank.features.login.LoginScreenActions
import com.deymer.ibank.features.register.RegisterScreen
import com.deymer.ibank.features.register.RegisterScreenActions
import com.deymer.ibank.features.splash.SplashScreen
import com.deymer.ibank.features.splash.SplashScreenActions
import com.deymer.ibank.navigation.AppScreens.SplashScreen
import com.deymer.ibank.navigation.AppScreens.LoginScreen
import com.deymer.ibank.navigation.AppScreens.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SplashScreen.route
    ) {
        composable(route = SplashScreen.route) {
            SplashScreen(SplashScreenActions(
                onPrimaryAction = {
                    navController.popBackStack()
                    navController.navigate(route = LoginScreen.route)
                }
            ))
        }
        composable(route = LoginScreen.route) {
            LoginScreen(actions = LoginScreenActions(
                onPrimaryAction = {
                    navController.popBackStack()
                },
                onSecondaryAction = {
                    navController.navigate(route = RegisterScreen.route)
                }
            ))
        }
        composable(route = RegisterScreen.route) {
            RegisterScreen(actions = RegisterScreenActions(
                onPrimaryAction = { navController.popBackStack() },
                onSecondaryAction = {}
            ))
        }
    }
}