package org.prography.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(
    navHostController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    Text("HomeScreen")
}