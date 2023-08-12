package org.prography.cakk.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import org.prography.base.BaseBottomDestination
import org.prography.common.navigation.destination.CakkDestination
import org.prography.common.navigation.destination.CakkDestination.Home.DEFAULT_DISTRICTS_INFO
import org.prography.common.navigation.destination.CakkDestination.Home.DEFAULT_STORE_COUNT
import org.prography.common.navigation.destination.CakkDestination.Home.DISTRICTS_INFO
import org.prography.common.navigation.destination.CakkDestination.Home.STORE_COUNT
import org.prography.common.navigation.graph.GraphLabels
import org.prography.designsystem.ui.theme.Light_Deep_Pink
import org.prography.designsystem.ui.theme.Raisin_Black
import org.prography.designsystem.ui.theme.White
import org.prography.designsystem.ui.theme.pretendard
import org.prography.feed.FeedScreen
import org.prography.feed.detail.FeedDetailScreen
import org.prography.home.HomeScreen
import org.prography.home.detail.HomeDetailScreen
import org.prography.my.MyScreen
import org.prography.onboarding.OnBoardingScreen
import org.prography.splash.SplashScreen
import org.prography.utility.extensions.toSp

@Composable
fun CakkNavigationGraph(navController: NavHostController) {
    Scaffold(
        bottomBar = { CakkBottomBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = CakkDestination.Splash.route,
            modifier = Modifier.padding(paddingValues),
            route = GraphLabels.ROOT
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
                    }
                }
            }

            composable(route = CakkDestination.Feed.route) {
                FeedScreen { storeId ->
                    navController.navigate("${CakkDestination.FeedDetail.route}/$storeId")
                }
            }

            composable(
                route = CakkDestination.FeedDetail.routeWithArgs,
                arguments = CakkDestination.FeedDetail.arguments
            ) { navBackStackEntry ->
                val storeId = navBackStackEntry.arguments?.getInt(CakkDestination.FeedDetail.STORE_ID)
                storeId?.let {
                    FeedDetailScreen(
                        storeId = it,
                        onNavigateHomeDetail = {
                            navController.navigate("${CakkDestination.HomeDetail.route}/$storeId")
                        },
                        onClose = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(route = CakkDestination.My.route) {
                MyScreen()
            }
        }
    }
}

@Composable
private fun CakkBottomBar(navHostController: NavHostController) {
    val bottomDestinations = listOf(
        CakkDestination.Home,
        CakkDestination.Feed,
        CakkDestination.My
    )

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val hasBottomNavigation = bottomDestinations.any { destination ->
        currentDestination?.route == when (destination) {
            is CakkDestination.Home -> destination.routeWithArgs
            CakkDestination.Feed -> destination.route
            CakkDestination.My -> destination.route
            else -> IllegalStateException()
        }
    }

    if (hasBottomNavigation) {
        BottomNavigation(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = White
        ) {
            bottomDestinations.forEach { bottomDestination ->
                CakkBottomBarItem(
                    bottomDestination = bottomDestination,
                    currentDestination = currentDestination,
                    navHostController = navHostController
                )
            }
        }
    }
}

@Composable
private fun RowScope.CakkBottomBarItem(
    bottomDestination: CakkDestination,
    currentDestination: NavDestination?,
    navHostController: NavHostController
) {
    check(bottomDestination is BaseBottomDestination)
    val destinationRoute = when (bottomDestination) {
        is CakkDestination.Home -> bottomDestination.routeWithArgs
        CakkDestination.Feed -> bottomDestination.route
        CakkDestination.My -> bottomDestination.route
        else -> throw java.lang.IllegalStateException()
    }

    val selected = currentDestination?.route == destinationRoute
    BottomNavigationItem(
        selected = selected,
        onClick = {
            navHostController.navigate(destinationRoute) {
                popUpTo(destinationRoute) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        },
        icon = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(56.dp, 2.dp)
                        .background(if (selected) Light_Deep_Pink else Color.Transparent)
                )
                Icon(
                    painter = painterResource(bottomDestination.icon),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = if (selected) Light_Deep_Pink else Color.Unspecified
                )
            }
        },
        label = {
            Text(
                text = bottomDestination.label,
                color = Raisin_Black.copy(alpha = if (selected) 0.8f else 0.4f),
                fontSize = 10.dp.toSp(),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                fontFamily = pretendard
            )
        }
    )
}
