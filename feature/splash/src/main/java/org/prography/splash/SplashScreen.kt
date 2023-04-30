package org.prography.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.prography.designsystem.ui.theme.pretendard

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
) {
    LaunchedEffect(Unit) {
        delay(2000)
        navHostController.navigate("home") {
            popUpTo("splash") {
                inclusive = true
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size((26.2).dp))
            Text(
                modifier = Modifier
                    .background(Color(0xFF98A4E7), RoundedCornerShape(20.dp))
                    .padding(12.dp),
                text = stringResource(R.string.splash_logo_description),
                color = Color.White,
                fontSize = 14.sp,
                letterSpacing = (-0.05).em,
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(
    widthDp = 540,
    heightDp = 1320,
    showBackground = true,
)
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
