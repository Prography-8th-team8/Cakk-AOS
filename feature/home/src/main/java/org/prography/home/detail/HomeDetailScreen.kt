package org.prography.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.prography.cakk.data.api.model.enums.StoreType
import org.prography.designsystem.R
import org.prography.designsystem.extensions.toColor
import org.prography.designsystem.ui.theme.*
import org.prography.home.detail.BlogReviewModel
import org.prography.home.detail.HomeDetailAction
import org.prography.home.detail.HomeDetailViewModel
import org.prography.utility.extensions.toSp

@Composable
fun HomeDetailScreen(
    navHostController: NavHostController = rememberNavController(),
    homeDetailViewModel: HomeDetailViewModel = hiltViewModel(),
    storeId: Int,
) {
    LaunchedEffect(storeId) {
        homeDetailViewModel.sendAction(HomeDetailAction.LoadDetailInfo(storeId))
    }

    val storeDetailState = homeDetailViewModel.state.collectAsStateWithLifecycle()
    val storeDetailModel = storeDetailState.value.storeDetailModel
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        HomeDetailAppbar(
            title = stringResource(R.string.home_detail_app_bar),
            onBack = { navHostController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_default_cakeshop),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(360 / 176f)
            )

            Text(
                text = storeDetailModel.name,
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
                text = storeDetailModel.location,
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

            if (storeDetailModel.storeTypes.isNotEmpty()) {
                HomeDetailKeywordRow(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth(),
                    storeTypes = storeDetailModel.storeTypes.map { StoreType.valueOf(it) }
                )
            }

            Spacer(
                modifier = Modifier
                    .padding(top = if (storeDetailModel.storeTypes.isNotEmpty()) 22.dp else 32.dp)
                    .height(10.dp)
                    .fillMaxWidth()
                    .background(Platinum)
            )

            HomeDetailInfoRow(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                location = storeDetailModel.location
            )

            Spacer(
                modifier = Modifier
                    .padding(top = 26.5.dp)
                    .height(10.dp)
                    .fillMaxWidth()
                    .background(Platinum)
            )

            HomeDetailBlogRow(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 60.dp)
                    .padding(horizontal = 16.dp),
                blogReviews = listOf(
                    BlogReviewModel(
                        name = "블로거 이름",
                        date = "2023.05.01",
                        title = "제목을 입력해 주세요. 제목은 한 줄까지만 노출 되게 제목을 입력해 주세요. 제목은 한 줄까지만 노출 되게",
                        description = "블로그 내용을 입력해 주세요. 내용은 최대 3줄까지만 쓰게 하려고 해요. 이 섹션 전체를 눌렀을 때 네이버로 이동할 수 있게 해당 블로그 글 링크 연결을 하면 괜찮지 않을까요? 어떻게 생각...",
                    ),
                    BlogReviewModel(
                        name = "블로거 이름",
                        date = "2023.05.01",
                        title = "제목을 입력해 주세요. 제목은 한 줄까지만 노출 되게 제목을 입력해 주세요. 제목은 한 줄까지만 노출 되게",
                        description = "블로그 내용을 입력해 주세요. 내용은 최대 3줄까지만 쓰게 하려고 해요. 이 섹션 전체를 눌렀을 때 네이버로 이동할 수 있게 해당 블로그 글 링크 연결을 하면 괜찮지 않을까요? 어떻게 생각...",
                    ),
                    BlogReviewModel(
                        name = "블로거 이름",
                        date = "2023.05.01",
                        title = "제목을 입력해 주세요. 제목은 한 줄까지만 노출 되게 제목을 입력해 주세요. 제목은 한 줄까지만 노출 되게",
                        description = "블로그 내용을 입력해 주세요. 내용은 최대 3줄까지만 쓰게 하려고 해요. 이 섹션 전체를 눌렀을 때 네이버로 이동할 수 있게 해당 블로그 글 링크 연결을 하면 괜찮지 않을까요? 어떻게 생각...",
                    )
                )
            )
        }
    }
}

@Composable
private fun HomeDetailBlogRow(
    modifier: Modifier = Modifier,
    blogReviews: List<BlogReviewModel>,
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.home_detail_blog_review),
            color = Raisin_Black,
            fontSize = 18.dp.toSp(),
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.03).em
        )

        Spacer(
            modifier = Modifier
                .padding(top = 24.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(Platinum)
        )

        blogReviews.take(3).forEach { review ->
            HomeDetailBlogItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 23.dp),
                blogReview = review
            )

            Spacer(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Platinum)
            )
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Raisin_Black.copy(alpha = 0.1f)),
            elevation = ButtonDefaults.elevation(0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = White
            )
        ) {
            Text(
                text = "블로그 리뷰 더 보기",
                color = Raisin_Black,
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.03).em
            )
        }
    }
}

@Composable
private fun HomeDetailBlogItem(
    modifier: Modifier = Modifier,
    blogReview: BlogReviewModel,
) {
    Column(modifier) {
        Row {
            Text(
                text = blogReview.name,
                modifier = Modifier.weight(1f, false),
                color = Raisin_Black,
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = (-0.03).em
            )

            Text(
                text = "∙",
                modifier = Modifier.padding(start = 4.dp),
                color = Raisin_Black.copy(alpha = 0.2f),
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard
            )

            Text(
                text = blogReview.date,
                modifier = Modifier.padding(start = 4.dp),
                color = Raisin_Black.copy(alpha = 0.6f),
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard,
                letterSpacing = (-0.03).em
            )
        }

        Text(
            text = blogReview.title,
            modifier = Modifier.padding(top = 16.dp),
            color = Raisin_Black,
            fontSize = 16.dp.toSp(),
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            letterSpacing = (-0.03).em
        )

        Text(
            text = blogReview.description,
            modifier = Modifier.padding(top = 12.dp),
            color = Raisin_Black.copy(alpha = 0.8f),
            fontSize = 14.dp.toSp(),
            fontFamily = pretendard,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            letterSpacing = (-0.03).em,
            lineHeight = (22.4).dp.toSp()
        )
    }
}

@Composable
private fun HomeDetailInfoRow(
    modifier: Modifier = Modifier,
    location: String,
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.home_detail_info),
            color = Raisin_Black,
            fontSize = 18.dp.toSp(),
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.03).em
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(top = 24.dp)
                .background(Platinum)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_location),
                contentDescription = null,
            )

            Text(
                text = location,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f, false),
                color = Raisin_Black,
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard,
                letterSpacing = (-0.03).em,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "∙",
                modifier = Modifier.padding(start = 4.dp),
                color = Raisin_Black.copy(alpha = 0.2f),
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard
            )

            Text(
                text = "주소 복사",
                modifier = Modifier.padding(start = 4.dp),
                color = Raisin_Black,
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.03).em
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(top = 16.dp)
                .background(Platinum)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 17.5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_clock),
                contentDescription = null
            )

            Text(
                text = stringResource(R.string.home_detail_opening),
                modifier = Modifier.padding(start = 12.dp),
                color = Raisin_Black,
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.03).em
            )

            Text(
                text = "∙",
                modifier = Modifier.padding(start = 4.dp),
                color = Raisin_Black.copy(alpha = 0.2f),
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard
            )

            // TODO 변경 예정
            Text(
                text = "22:30에 영업 종료",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f, false),
                color = Raisin_Black,
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard
            )

            Image(
                painter = painterResource(R.drawable.ic_down_arrow),
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun HomeDetailKeywordRow(
    modifier: Modifier = Modifier,
    storeTypes: List<StoreType>,
) {
    Column(modifier) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Platinum)
        )

        Text(
            text = stringResource(R.string.home_detail_keyword),
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(top = 40.dp),
            color = Raisin_Black,
            fontSize = 18.dp.toSp(),
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.03).em
        )

        LazyRow(
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(storeTypes, key = { it.tag }) {
                Text(
                    text = it.tag,
                    modifier = Modifier
                        .background(it.toColor().copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    color = it.toColor(),
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
    Row(
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Max)
    ) {
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