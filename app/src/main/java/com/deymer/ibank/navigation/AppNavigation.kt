package com.deymer.ibank.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.deymer.ibank.features.home.HomeScreen
import com.deymer.ibank.features.home.HomeScreenActions
import com.deymer.ibank.features.login.LoginScreen
import com.deymer.ibank.features.login.LoginScreenActions
import com.deymer.ibank.features.profile.ProfileScreen
import com.deymer.ibank.features.profile.ProfileScreenActions
import com.deymer.ibank.features.register.RegisterScreen
import com.deymer.ibank.features.register.RegisterScreenActions
import com.deymer.ibank.features.splash.SplashScreen
import com.deymer.ibank.features.splash.SplashScreenActions
import com.deymer.ibank.features.transaction.TransactionDetailScreen
import com.deymer.ibank.features.transaction.TransactionDetailsActions
import com.deymer.ibank.features.transaction.TransactionDetailsAttributes
import com.deymer.ibank.navigation.AppScreens.SplashScreen
import com.deymer.ibank.navigation.AppScreens.LoginScreen
import com.deymer.ibank.navigation.AppScreens.RegisterScreen
import com.deymer.ibank.navigation.AppScreens.HomeScreen
import com.deymer.ibank.navigation.AppScreens.AuthGraph
import com.deymer.ibank.navigation.AppScreens.UserGraph
import com.deymer.ibank.navigation.AppScreens.ProfileScreen
import com.deymer.ibank.navigation.AppScreens.TransactionDetailScreen
import com.deymer.ibank.navigation.RouteArguments.TRANSACTION_ID

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SplashScreen.route
    ) {
        composable(route = SplashScreen.route) {
            SplashScreen(actions = SplashScreenActions(
                onPrimaryAction = {
                    navController.popBackStack()
                    navController.navigate(route = HomeScreen.route)
                },
                onSecondaryAction = {
                    navController.popBackStack()
                    navController.navigate(route = LoginScreen.route)
                }
            ))
        }
        navigation(startDestination = LoginScreen.route, route = AuthGraph.route) {
            composable(route = LoginScreen.route) {
                LoginScreen(actions = LoginScreenActions(
                    onPrimaryAction = {
                        navController.navigate(HomeScreen.route) {
                            popUpTo(AuthGraph.route) { inclusive = true }
                        }
                    },
                    onSecondaryAction = {
                        navController.navigate(route = RegisterScreen.route)
                    }
                ))
            }
            composable(route = RegisterScreen.route) {
                RegisterScreen(actions = RegisterScreenActions(
                    onPrimaryAction = {
                        navController.navigate(HomeScreen.route) {
                            popUpTo(AuthGraph.route) { inclusive = true }
                        }
                    },
                    onSecondaryAction = {
                        navController.popBackStack()
                    }
                ))
            }
        }
        navigation(startDestination = HomeScreen.route, route = UserGraph.route) {
            composable(route = HomeScreen.route) {
                HomeScreen(actions = HomeScreenActions(
                    onPrimaryAction = {
                        navController.navigate(ProfileScreen.route)
                    },
                    onSecondaryAction = { transactionId ->
                        navController.navigate(
                            route = "${TransactionDetailScreen.route}/$transactionId"
                        )
                    }
                ))
            }
            composable(route = ProfileScreen.route) {
                ProfileScreen(actions = ProfileScreenActions(
                    onPrimaryAction = {
                        navController.popBackStack()
                    },
                    onSecondaryAction = {
                        navController.navigate(AuthGraph.route) {
                            popUpTo(UserGraph.route) { inclusive = true }
                        }
                    }
                ))
            }
        }
        composable(
            route = "${TransactionDetailScreen.route}/{$TRANSACTION_ID}",
            arguments = listOf(navArgument(name = TRANSACTION_ID) {
                type = StringType
            })
        ) {
            TransactionDetailScreen(
                attributes = TransactionDetailsAttributes(
                    transactionId = it.arguments?.getString(TRANSACTION_ID).orEmpty(),
                    actions = TransactionDetailsActions(
                        onPrimaryAction = {
                            navController.popBackStack()
                        }
                    )
                )
            )
        }
    }
}