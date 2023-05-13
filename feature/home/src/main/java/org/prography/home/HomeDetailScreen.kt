package org.prography.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.prography.designsystem.R
import org.prography.designsystem.ui.theme.Black
import org.prography.designsystem.ui.theme.pretendard
import org.prography.utility.extensions.toSp

@Composable
fun HomeDetailScreen(
    navHostController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
}

@Composable
fun HomeDetailAppbar(
    title: String,
    onBack: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.ic_left_arrow),
            contentDescription = title,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .padding(vertical = 9.dp)
                .clickable(onClick = onBack)
        )

        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center).padding(vertical = 9.dp),
            color = Black,
            fontSize = 17.dp.toSp(),
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.02).em
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeDetailAppbarPreview() {
    HomeDetailAppbar(
        title = stringResource(R.string.home_detail_app_bar)
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeDetailScreenPreview() {
    HomeDetailScreen()
}
