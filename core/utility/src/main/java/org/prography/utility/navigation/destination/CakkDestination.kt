package org.prography.utility.navigation.destination

sealed class CakkDestination(
    val route: String,
) {
    object Splash : CakkDestination(route = SPLASH)
    object Home : CakkDestination(route = HOME)
    object OnBoarding : CakkDestination(route = ON_BOARDING)

    companion object {
        private const val SPLASH = "splash"
        private const val HOME = "home"
        private const val ON_BOARDING = "onBoarding"
    }

    fun withArgs(vararg args: Boolean): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
