package org.prography.cakk.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.prography.home.HomeScreen
import org.prography.onboarding.OnBoardingScreen
import org.prography.splash.SplashScreen
import org.prography.utility.navgraph.GraphLabels

@Composable
fun CakkNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = GraphLabels.ROOT,
        startDestination = CakkDestination.Splash.route,
    ) {
        composable(route = CakkDestination.Splash.route) {
            SplashScreen(
                navHostController = navController,
                modifier = Modifier.fillMaxSize(),
            )
        }

        composable(route = CakkDestination.Home.route) {
            HomeScreen()
        }

        composable(route = CakkDestination.OnBoarding.route) {
            OnBoardingScreen()
        }
    }
}
