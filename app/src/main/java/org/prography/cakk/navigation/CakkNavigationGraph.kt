package org.prography.cakk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.prography.home.HomeDetailScreen
import org.prography.home.HomeScreen
import org.prography.onboarding.OnBoardingScreen
import org.prography.splash.SplashScreen
import org.prography.utility.navigation.destination.CakkDestination
import org.prography.utility.navigation.graph.GraphLabels

@Composable
fun CakkNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = GraphLabels.ROOT,
        startDestination = CakkDestination.Splash.route,
    ) {
        composable(route = CakkDestination.Splash.route) {
            SplashScreen(navHostController = navController)
        }

        composable(
            route = CakkDestination.Home.routeWithArgs,
            arguments = CakkDestination.Home.arguments
        ) { navBackStackEntry ->
            val districts = navBackStackEntry.arguments?.getString(CakkDestination.Home.DISTRICTS_INFO)
            val storeCount = navBackStackEntry.arguments?.getInt(CakkDestination.Home.STORE_COUNT)
            HomeScreen(navHostController = navController, districtsArg = districts, storeCountArg = storeCount)
        }

        composable(
            route = CakkDestination.HomeDetail.routeWithArgs,
            arguments = CakkDestination.HomeDetail.arguments
        ) { navBackStackEntry ->
            val storeId = navBackStackEntry.arguments?.getInt(CakkDestination.HomeDetail.STORE_ID)
            storeId?.let { HomeDetailScreen(navHostController = navController, storeId = it) }
        }

        composable(route = CakkDestination.OnBoarding.route) {
            OnBoardingScreen(navHostController = navController)
        }
    }
}
