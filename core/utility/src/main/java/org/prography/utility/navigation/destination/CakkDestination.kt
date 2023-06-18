package org.prography.utility.navigation.destination

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class CakkDestination(
    val route: String,
) {
    object Splash : CakkDestination(route = SPLASH)
    object Home : CakkDestination(route = HOME) {
        const val DISTRICTS_INFO = "DISTRICTS_INFO"
        const val STORE_COUNT = "STORE_COUNT"
        val routeWithArgs = "$route?$DISTRICTS_INFO={$DISTRICTS_INFO}&$STORE_COUNT={$STORE_COUNT}"
        val arguments = listOf(
            navArgument(DISTRICTS_INFO) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(STORE_COUNT) {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    }

    object HomeDetail : CakkDestination(route = HOME_DETAIL) {
        const val STORE_ID = "STORE_ID"
        val routeWithArgs = "$route/{$STORE_ID}"
        val arguments = listOf(
            navArgument(STORE_ID) { type = NavType.IntType }
        )
    }

    object OnBoarding : CakkDestination(route = ON_BOARDING)

    companion object {
        private const val SPLASH = "splash"
        private const val HOME = "home"
        private const val HOME_DETAIL = "home_detail"
        private const val ON_BOARDING = "onBoarding"
    }
}
