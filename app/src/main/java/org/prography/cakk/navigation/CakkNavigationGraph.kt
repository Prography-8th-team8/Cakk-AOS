package org.prography.cakk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.prography.home.detail.HomeDetailScreen
import org.prography.home.HomeScreen
import org.prography.onboarding.OnBoardingScreen
import org.prography.splash.SplashScreen
import org.prography.utility.navigation.destination.CakkDestination
import org.prography.utility.navigation.destination.CakkDestination.Home.DEFAULT_DISTRICTS_INFO
import org.prography.utility.navigation.destination.CakkDestination.Home.DEFAULT_STORE_COUNT
import org.prography.utility.navigation.destination.CakkDestination.Home.DISTRICTS_INFO
import org.prography.utility.navigation.destination.CakkDestination.Home.STORE_COUNT
import org.prography.utility.navigation.graph.GraphLabels

@Composable
fun CakkNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = GraphLabels.ROOT,
        startDestination = CakkDestination.Splash.route,
    ) {
        composable(route = CakkDestination.Splash.route) {
            SplashScreen {
                navController.navigate(CakkDestination.Home.routeWithArgs) {
                    popUpTo(CakkDestination.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(
            route = CakkDestination.Home.routeWithArgs,
            arguments = CakkDestination.Home.arguments
        ) { navBackStackEntry ->
            val districts = navBackStackEntry.arguments?.getString(DISTRICTS_INFO) ?: DEFAULT_DISTRICTS_INFO
            val storeCount = navBackStackEntry.arguments?.getInt(STORE_COUNT) ?: DEFAULT_STORE_COUNT
            HomeScreen(
                districtsArg = districts,
                storeCountArg = storeCount,
                onNavigateToOnBoarding = {
                    navController.navigate(CakkDestination.OnBoarding.route) {
                        popUpTo(CakkDestination.Home.routeWithArgs) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToDetail = { storeId ->
                    navController.navigate("${CakkDestination.HomeDetail.route}/$storeId")
                }
            )
        }

        composable(
            route = CakkDestination.HomeDetail.routeWithArgs,
            arguments = CakkDestination.HomeDetail.arguments
        ) { navBackStackEntry ->
            val storeId = navBackStackEntry.arguments?.getInt(CakkDestination.HomeDetail.STORE_ID)
            storeId?.let {
                HomeDetailScreen(storeId = it) {
                    navController.popBackStack()
                }
            }
        }

        composable(route = CakkDestination.OnBoarding.route) {
            OnBoardingScreen { districts, storeCount ->
                navController.navigate(
                    CakkDestination.Home.route + "?" +
                        "$DISTRICTS_INFO=$districts" + "&" +
                        "$STORE_COUNT=$storeCount"
                ) {
                    popUpTo(CakkDestination.OnBoarding.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }
}
