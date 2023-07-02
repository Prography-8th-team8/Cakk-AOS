package org.prography.home

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.Html.FROM_HTML_OPTION_USE_CSS_COLORS
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
import androidx.compose.ui.platform.LocalContext
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
import org.prography.designsystem.mapper.toColor
import org.prography.designsystem.ui.theme.*
import org.prography.domain.model.store.BlogPostModel
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
        homeDetailViewModel.sendAction(HomeDetailAction.LoadBlogInfos(storeId))
    }

    val storeDetailState = homeDetailViewModel.state.collectAsStateWithLifecycle()
    val storeDetailModel = storeDetailState.value.storeDetailModel
    val storeBlogPosts = storeDetailState.value.blogPosts
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        HomeDetailAppbar(title = stringResource(R.string.home_detail_app_bar)) {
            navHostController.popBackStack()
        }

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
                blogPosts = storeBlogPosts
            )
        }
    }
}

@Composable
private fun HomeDetailBlogRow(
    modifier: Modifier = Modifier,
    blogPosts: List<BlogPostModel>,
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

        blogPosts.take(3).forEach { blogPost ->
            HomeDetailBlogItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 23.dp),
                blogPost = blogPost
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
                text = stringResource(R.string.home_detail_blog_review_more),
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
    blogPost: BlogPostModel,
) {
    val year = blogPost.postdate.substring(0, 4)
    val month = blogPost.postdate.substring(4, 6)
    val day = blogPost.postdate.substring(6)

    val context = LocalContext.current
    Column(
        modifier = modifier.clickable {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(blogPost.link)))
        }
    ) {
        Row {
            Text(
                text = Html.fromHtml(blogPost.bloggername, FROM_HTML_OPTION_USE_CSS_COLORS).toString(),
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
                text = "$year.$month.$day",
                modifier = Modifier.padding(start = 4.dp),
                color = Raisin_Black.copy(alpha = 0.6f),
                fontSize = 14.dp.toSp(),
                fontFamily = pretendard,
                letterSpacing = (-0.03).em
            )
        }

        Text(
            text = Html.fromHtml(blogPost.title, FROM_HTML_OPTION_USE_CSS_COLORS).toString(),
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
            text = Html.fromHtml(blogPost.description, FROM_HTML_OPTION_USE_CSS_COLORS).toString(),
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
                text = stringResource(R.string.home_detail_address_copy),
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
