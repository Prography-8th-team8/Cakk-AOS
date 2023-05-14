package org.prography.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.prography.designsystem.R
import org.prography.designsystem.ui.theme.*
import org.prography.utility.extensions.toSp

@Composable
fun HomeDetailScreen(
    navHostController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    cake_shop_brand: String,
    cake_shop_location: String,
    cake_shop_keywords: List<StoreType>,
) {
    Column(modifier) {
        HomeDetailAppbar(
            title = stringResource(R.string.home_detail_app_bar),
            onBack = {}
        )

        Image(
            painter = painterResource(R.drawable.img_default_cakeshop),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(360 / 176f)
        )

        Text(
            text = cake_shop_brand,
            modifier = Modifier
                .padding(top = 40.dp)
                .align(Alignment.CenterHorizontally),
            color = Raisin_Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.dp.toSp(),
            fontFamily = pretendard,
            letterSpacing = (-0.03).em
        )

        Text(
            text = cake_shop_location,
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            color = Raisin_Black.copy(alpha = 0.8f),
            fontSize = 16.dp.toSp(),
            fontFamily = pretendard,
            letterSpacing = (-0.03).em
        )

        HomeDetailTab(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
        )

        Spacer(
            modifier = Modifier
                .padding(top = 32.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        )

        HomeDetailKeywordRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            cake_shop_keywords = cake_shop_keywords
        )
    }
}

@Composable
private fun HomeDetailKeywordRow(
    modifier: Modifier = Modifier,
    cake_shop_keywords: List<StoreType>,
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.home_detail_keyword),
            modifier = Modifier.padding(top = 40.dp),
            color = Raisin_Black,
            fontSize = 18.dp.toSp(),
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.03).em
        )

        LazyRow(
            modifier = Modifier.padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(cake_shop_keywords, key = { it.tag }) {
                Text(
                    text = it.tag,
                    modifier = Modifier
                        .background(it.color.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    color = it.color,
                    fontSize = 12.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendard,
                    letterSpacing = (-0.03).em,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun HomeDetailTab(
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.height(intrinsicSize = IntrinsicSize.Max)) {
        HomeDetailTabItem(
            drawableRes = R.drawable.ic_phone,
            stringRes = R.string.home_detail_phone
        )

        Spacer(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.Gray)
        )

        HomeDetailTabItem(
            drawableRes = R.drawable.ic_bookmark,
            stringRes = R.string.home_detail_bookmark
        )

        Spacer(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.Gray)
        )

        HomeDetailTabItem(
            drawableRes = R.drawable.ic_navigation,
            stringRes = R.string.home_detail_navigation
        )

        Spacer(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.Gray)
        )

        HomeDetailTabItem(
            drawableRes = R.drawable.ic_share,
            stringRes = R.string.home_detail_share
        )
    }
}

@Composable
private fun RowScope.HomeDetailTabItem(
    @DrawableRes drawableRes: Int,
    @StringRes stringRes: Int,
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(drawableRes),
            contentDescription = null,
            modifier = Modifier.padding(top = 9.dp)
        )

        Text(
            text = stringResource(stringRes),
            modifier = Modifier.padding(top = 11.dp),
            color = Raisin_Black.copy(alpha = 0.8f),
            fontSize = 14.dp.toSp(),
            fontFamily = pretendard,
            letterSpacing = (-0.03).em
        )
    }
}

@Composable
private fun HomeDetailAppbar(
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
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 9.dp),
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
private fun HomeDetailKeywordPreview() {
    HomeDetailKeywordRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        cake_shop_keywords = listOf(
            StoreType("캐릭터", Light_Deep_Pink),
            StoreType("레터링", Palatinate_Blue),
            StoreType("떡케이크", Medium_Slate_Blue),
            StoreType("도시락", Mustard_Yellow),
            StoreType("플라워", Metallic_Sunburst),
            StoreType("포토", Congo_Pink),
            StoreType("피규어", Yankees_Blue),
            StoreType("업체 케이크", Light_Deep_Pink),
            StoreType("티아라", Cerise)
        )
    )
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
private fun HomeDetailTabPreview() {
    HomeDetailTab(
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeDetailScreenPreview() {
    HomeDetailScreen(
        modifier = Modifier.fillMaxSize(),
        cake_shop_brand = "케이크를 부탁해 연신내역점",
        cake_shop_location = "서울 은평구 연서로29길 8",
        cake_shop_keywords = listOf(
            StoreType("캐릭터", Light_Deep_Pink),
            StoreType("레터링", Palatinate_Blue),
            StoreType("떡케이크", Medium_Slate_Blue),
            StoreType("도시락", Mustard_Yellow),
            StoreType("플라워", Metallic_Sunburst),
            StoreType("포토", Congo_Pink),
            StoreType("피규어", Yankees_Blue),
            StoreType("업체 케이크", Light_Deep_Pink),
            StoreType("티아라", Cerise)
        )
    )
}
