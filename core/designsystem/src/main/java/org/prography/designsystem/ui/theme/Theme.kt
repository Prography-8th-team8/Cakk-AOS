package org.prography.designsystem.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorPalette = darkColors(
    primary = Violet,
    primaryVariant = Interdimensional_Blue,
    secondary = Egg_Blue
)

private val LightColorPalette = lightColors(
    primary = Indigo,
    primaryVariant = Interdimensional_Blue,
    secondary = Egg_Blue
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CakkTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ) {
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null,
            content = content,
        )
    }
}
