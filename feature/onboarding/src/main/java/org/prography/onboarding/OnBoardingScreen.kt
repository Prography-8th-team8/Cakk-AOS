package org.prography.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.prography.designsystem.ui.theme.Black
import org.prography.designsystem.ui.theme.White
import org.prography.designsystem.ui.theme.pretendard
import org.prography.utility.extensions.toSp

@Composable
fun OnBoardingScreen(navHostController: NavHostController = rememberNavController()) {
    val list = listOf(
        RegionItem(0, "도봉 강북 성북 노원", 43, Color(0x1A2448FF)),
        RegionItem(1, "동대문 중랑 성동 광진", 45, Color(0x33FF857D)),
        RegionItem(2, "은평 마포 서대문", 77, Color(0x26FF5CBE)),
        RegionItem(3, "종로 중구 용산", 20, Color(0x33FEDC4D)),
        RegionItem(4, "강서 양천 영등포 구로", 34, Color(0x66FF857D)),
        RegionItem(5, "동작 관악 금천", 28, Color(0x332448FF)),
        RegionItem(6, "서초 강남", 48, Color(0x4DFEDC4D)),
        RegionItem(7, "강동 송파", 48, Color(0x4DFF5CBE))
    )

    Column(
        modifier = Modifier.fillMaxSize().background(White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.Start),
            text = stringResource(R.string.onboarding_title),
            color = Black,
            fontSize = 20.dp.toSp(),
            letterSpacing = (-0.03).em,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp)
                .align(Alignment.Start),
            text = stringResource(R.string.onboarding_subtitle),
            color = Black.copy(alpha = 0.6f),
            fontSize = 16.dp.toSp(),
            letterSpacing = (-0.03).em,
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal
        )

        LazyVerticalGrid(
            modifier = Modifier.padding(top = 38.dp, start = 6.dp, end = 6.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(list, key = { it.id }) {
                OnBoardingRegionItem(
                    modifier = Modifier.size(170.dp, 120.dp),
                    region = it.region,
                    count = it.count,
                    color = it.color,
                    onClick = {}
                )
            }
        }

        Text(
            modifier = Modifier.padding(top = 40.dp),
            text = stringResource(R.string.onboarding_not_find_region),
            color = Black,
            fontSize = 14.dp.toSp(),
            letterSpacing = (-0.03).em,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.onboarding_request_region),
            color = Black,
            fontSize = 14.dp.toSp(),
            letterSpacing = (-0.03).em,
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
private fun OnBoardingRegionItem(
    modifier: Modifier = Modifier,
    region: String,
    count: Int,
    color: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(2.dp)
            .background(Black.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(top = 1.dp, start = 2.dp, end = 2.dp, bottom = 4.dp)
                .background(White, RoundedCornerShape(24.dp))
                .background(color, RoundedCornerShape(24.dp))
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 24.dp, start = 20.dp),
                text = region,
                color = Black.copy(0.8f),
                fontSize = 14.dp.toSp(),
                letterSpacing = (-0.03).em,
                fontFamily = pretendard,
                fontWeight = FontWeight.Medium
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp, start = 20.dp, end = 20.dp),
                text = "${count}개",
                textAlign = TextAlign.End,
                color = Black,
                fontSize = 20.dp.toSp(),
                letterSpacing = (-0.03).em,
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnBoardingScreenPreview() {
    OnBoardingScreen()
}

@Preview
@Composable
private fun OnBoardingRegionItemPreview() {
    OnBoardingRegionItem(
        modifier = Modifier.size(170.dp, 120.dp),
        region = "도봉 강북 성북 노원",
        count = 43,
        color = Color(0x1A2448FF),
        onClick = {}
    )
}
