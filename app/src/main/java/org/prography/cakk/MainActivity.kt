package org.prography.cakk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.prography.cakk.navigation.CakkNavigationGraph
import org.prography.designsystem.ui.theme.CakkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CakkApp()
        }
    }
}

@Composable
fun CakkApp() {
    CakkTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            CakkNavigationGraph(navController = rememberNavController())
        }
    }
}
