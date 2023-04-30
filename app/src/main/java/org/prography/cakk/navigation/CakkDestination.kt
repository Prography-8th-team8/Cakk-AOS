package org.prography.cakk.navigation

sealed class CakkDestination(
    val route: String
) {
    object Splash : CakkDestination(route = SPLASH)
    object Home : CakkDestination(route = HOME)
    object OnBoarding : CakkDestination(route = ON_BOARDING)

    companion object {
        private const val SPLASH = "splash"
        private const val HOME = "home"
        private const val ON_BOARDING = "onBoarding"
    }
}
