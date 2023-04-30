package org.prography.splash

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun SplashScreen(
    navHostController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    Text(text = "SplashScreen")
}
