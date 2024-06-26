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
import com.deymer.ibank.features.recharge.RechargeScreen
import com.deymer.ibank.features.recharge.RechargeScreenActions
import com.deymer.ibank.features.register.RegisterScreen
import com.deymer.ibank.features.register.RegisterScreenActions
import com.deymer.ibank.features.splash.SplashScreen
import com.deymer.ibank.features.splash.SplashScreenActions
import com.deymer.ibank.features.transaction.details.DetailsActions
import com.deymer.ibank.features.transaction.details.DetailsAttributes
import com.deymer.ibank.features.transaction.details.DetailsScreen
import com.deymer.ibank.features.transaction.transfer.TransferScreenActions
import com.deymer.ibank.features.transaction.transfer.TransferScreen
import com.deymer.ibank.navigation.AppScreens.SplashScreen
import com.deymer.ibank.navigation.AppScreens.LoginScreen
import com.deymer.ibank.navigation.AppScreens.RegisterScreen
import com.deymer.ibank.navigation.AppScreens.HomeScreen
import com.deymer.ibank.navigation.AppScreens.AuthGraph
import com.deymer.ibank.navigation.AppScreens.UserGraph
import com.deymer.ibank.navigation.AppScreens.ProfileScreen
import com.deymer.ibank.navigation.AppScreens.DetailsScreen
import com.deymer.ibank.navigation.AppScreens.RechargeScreen
import com.deymer.ibank.navigation.AppScreens.TransferScreen
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
                            route = "${DetailsScreen.route}/$transactionId"
                        )
                    },
                    onTertiaryAction = {
                        navController.navigate(RechargeScreen.route)
                    },
                    onQuaternaryAction = {
                        navController.navigate(TransferScreen.route)
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
            route = "${DetailsScreen.route}/{$TRANSACTION_ID}",
            arguments = listOf(navArgument(name = TRANSACTION_ID) {
                type = StringType
            })
        ) {
            DetailsScreen(
                attributes = DetailsAttributes(
                    transactionId = it.arguments?.getString(TRANSACTION_ID).orEmpty(),
                    actions = DetailsActions(
                        onPrimaryAction = {
                            navController.popBackStack()
                        }
                    )
                )
            )
        }
        composable(route = RechargeScreen.route) {
            RechargeScreen(actions = RechargeScreenActions(
                onPrimaryAction = {
                    navController.popBackStack()
                }
            ))
        }
        composable(route = TransferScreen.route) {
            TransferScreen(actions = TransferScreenActions(
                onPrimaryAction = {
                    navController.popBackStack()
                }
            ))
        }
    }
}