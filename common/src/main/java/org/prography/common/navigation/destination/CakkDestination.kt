package org.prography.common.navigation.destination

import androidx.navigation.NavType
import androidx.navigation.navArgument
import org.prography.base.BaseBottomDestination
import org.prography.designsystem.R

sealed class CakkDestination(
    val route: String,
) {
    object Splash : CakkDestination(route = SPLASH)
    object Home : CakkDestination(route = HOME), BaseBottomDestination {
        override val label = HOME
        override val icon = R.drawable.ic_tabbar_home

        const val DISTRICTS_INFO = "DISTRICTS_INFO"
        const val STORE_COUNT = "STORE_COUNT"
        const val DEFAULT_DISTRICTS_INFO = ""
        const val DEFAULT_STORE_COUNT = -1

        val routeWithArgs = "$route?$DISTRICTS_INFO={$DISTRICTS_INFO}&$STORE_COUNT={$STORE_COUNT}"
        val arguments = listOf(
            navArgument(DISTRICTS_INFO) {
                type = NavType.StringType
                defaultValue = DEFAULT_DISTRICTS_INFO
            },
            navArgument(STORE_COUNT) {
                type = NavType.IntType
                defaultValue = DEFAULT_STORE_COUNT
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

    object Feed : CakkDestination(route = FEED), BaseBottomDestination {
        override val label = FEED
        override val icon = R.drawable.ic_feed
    }

    object FeedDetail : CakkDestination(route = FEED_DETAIL) {
        const val STORE_ID = "STORE_ID"
        val routeWithArgs = "$route/{$STORE_ID}"
        val arguments = listOf(
            navArgument(STORE_ID) { type = NavType.IntType }
        )
    }

    object My : CakkDestination(route = MY), BaseBottomDestination {
        override val label = MY
        override val icon = R.drawable.ic_tabbar_my
    }

    companion object {
        private const val SPLASH = "SPLASH"
        private const val HOME = "HOME"
        private const val HOME_DETAIL = "HOME_DETAIL"
        private const val ON_BOARDING = "ON_BOARDING"
        private const val FEED = "FEED"
        private const val FEED_DETAIL = "FEED_DETAIL"
        private const val MY = "MY"
    }
}
