package com.deymer.ibank.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deymer.ibank.features.login.LoginScreenAttributes
import com.deymer.ibank.features.login.LoginScreen
import com.deymer.ibank.features.register.RegisterScreenAttributes
import com.deymer.ibank.features.register.RegisterScreen
import com.deymer.ibank.features.splash.SplashScreen
import com.deymer.ibank.features.splash.SplashScreenAttributes
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
            SplashScreen(SplashScreenAttributes(
                onNavigateToLogin = {
                    navController.navigate(route = LoginScreen.route)
                }
            ))
        }
        composable(route = LoginScreen.route) {
            LoginScreen(LoginScreenAttributes(
                onNavigateToRegister = {
                    navController.navigate(route = RegisterScreen.route)
                },
                onLoginClick = {_, _ ->}
            ))
        }
        composable(route = RegisterScreen.route) {
            RegisterScreen(RegisterScreenAttributes(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterClick = {_, _, _, _, _ ->},
                onTakePhotoClick = {}
            ))
        }
        //composable(route = AppScreens.HomeScreen.route) {
        //    HomeScreen()
        //}
        //composable(route = AppScreens.TransactionDetailScreen.route) {
        //    TransactionDetailScreen()
        //}
        //composable(route = AppScreens.ProfileScreen.route) {
        //    ProfileScreen()
        //}
    }
}