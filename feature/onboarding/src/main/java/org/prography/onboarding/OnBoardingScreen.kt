package org.prography.onboarding

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun OnBoardingScreen(
    navHostController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    Text("OnBoardingScreen")
}