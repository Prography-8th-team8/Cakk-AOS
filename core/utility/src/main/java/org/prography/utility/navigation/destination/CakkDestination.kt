package org.prography.utility.navigation.destination

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class CakkDestination(
    val route: String,
) {
    object Splash : CakkDestination(route = SPLASH)
    object Home : CakkDestination(route = HOME) {
        const val districtsArgs = "districts"
        const val defaultValue = ""
        val routeWithArgs = "$route?$districtsArgs={$districtsArgs}"
        val arguments = listOf(
            navArgument(districtsArgs) {
                type = NavType.StringType
                defaultValue = Home.defaultValue
            }
        )
    }

    object HomeDetail : CakkDestination(route = HOME_DETAIL) {
        const val storeIdArg = "store_id"
        val routeWithArgs = "$route/{$storeIdArg}"
        val arguments = listOf(
            navArgument(storeIdArg) { type = NavType.IntType }
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
