package org.prography.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import org.prography.designsystem.ui.theme.pretendard
import org.prography.utility.extensions.toSp
import org.prography.utility.navigation.destination.CakkDestination

@Composable
fun SplashScreen(navHostController: NavHostController = rememberNavController()) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.Asset("grid_animation.json"))
        val logoAnimationState = animateLottieCompositionAsState(lottieComposition)

        LottieAnimation(
            composition = lottieComposition,
            progress = { logoAnimationState.progress },
            contentScale = ContentScale.FillHeight,
        )

        if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
            navHostController.navigate(CakkDestination.Home.route) {
                popUpTo(CakkDestination.Splash.route) {
                    inclusive = true
                }
            }
        }

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
                fontSize = 14.dp.toSp(),
                letterSpacing = (-0.05).em,
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
