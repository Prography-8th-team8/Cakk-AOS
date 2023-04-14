package org.prography.cakk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.prography.home.HomeScreen
import org.prography.onboarding.OnBoardingScreen
import org.prography.utility.navgraph.GraphLabels

@Composable
fun CakkNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = GraphLabels.ROOT,
        startDestination = CakkDestination.Home.route,
    ) {
        composable(route = CakkDestination.Home.route) {
            HomeScreen()
        }
        composable(route = CakkDestination.OnBoarding.route) {
            OnBoardingScreen()
        }
    }
}
