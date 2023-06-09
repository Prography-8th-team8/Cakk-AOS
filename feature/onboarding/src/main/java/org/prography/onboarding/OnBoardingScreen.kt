package org.prography.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.prography.designsystem.ui.theme.Black
import org.prography.designsystem.ui.theme.White
import org.prography.designsystem.ui.theme.pretendard
import org.prography.utility.extensions.toSp
import org.prography.utility.navigation.destination.CakkDestination

@Composable
fun OnBoardingScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    val state = viewModel.regions.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 6.dp)
            .background(White)
    ) {
        item(span = { GridItemSpan(2) }) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 74.dp),
                    text = stringResource(R.string.onboarding_title),
                    color = Black,
                    fontSize = 20.dp.toSp(),
                    letterSpacing = (-0.03).em,
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.padding(start = 10.dp, top = 12.dp),
                    text = stringResource(R.string.onboarding_subtitle),
                    color = Black.copy(alpha = 0.6f),
                    fontSize = 16.dp.toSp(),
                    letterSpacing = (-0.03).em,
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.size(38.dp))
            }
        }

        items(state.value) {
            OnBoardingRegionItem(
                modifier = Modifier
                    .padding(2.dp)
                    .aspectRatio(17 / 12f),
                region = it.region,
                count = it.count,
                color = it.color,
                onClick = {
                    // Home으로 이동, 추후 데이터까지 전달
                    navHostController.navigate(CakkDestination.Home.route) {
                        popUpTo(CakkDestination.OnBoarding.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.size(122.dp))
//            Column {
//                Text(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 40.dp),
//                    text = stringResource(R.string.onboarding_not_find_region),
//                    color = Black,
//                    fontSize = 14.dp.toSp(),
//                    letterSpacing = (-0.03).em,
//                    fontFamily = pretendard,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center
//                )
//
//                Text(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 8.dp, bottom = 46.dp)
//                        .clickable {
//                            // 지역 요청 API 호출 ,
//                        },
//                    text = stringResource(R.string.onboarding_request_region),
//                    color = Black,
//                    fontSize = 14.dp.toSp(),
//                    letterSpacing = (-0.03).em,
//                    fontFamily = pretendard,
//                    fontWeight = FontWeight.Normal,
//                    textDecoration = TextDecoration.Underline,
//                    textAlign = TextAlign.Center
//                )
//            }
        }
    }
}

@Composable
private fun OnBoardingRegionItem(
    modifier: Modifier = Modifier,
    region: String,
    count: Int,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(Black.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
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
                text = stringResource(id = R.string.onboarding_region_item_count, count),
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
