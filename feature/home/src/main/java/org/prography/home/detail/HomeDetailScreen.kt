package org.prography.home.detail

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import org.prography.designsystem.R
import org.prography.designsystem.component.CakkAppbarWithBack
import org.prography.designsystem.mapper.toColor
import org.prography.designsystem.ui.theme.*
import org.prography.domain.model.enums.StoreType
import org.prography.domain.model.store.BlogPostModel
import org.prography.domain.model.store.StoreDetailModel
import org.prography.utility.extensions.toSp

@Composable
fun HomeDetailScreen(
    homeDetailViewModel: HomeDetailViewModel = hiltViewModel(),
    storeId: Int,
    onBack: () -> Unit
) {
    LaunchedEffect(storeId) {
        homeDetailViewModel.fetchDetailStore(storeId)
        homeDetailViewModel.fetchStoreBlogInfos(storeId)
    }

    val storeDetailUiState = homeDetailViewModel.state.collectAsStateWithLifecycle().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        HomeDetailContent(
            storeDetailModel = storeDetailUiState.storeDetailModel,
            storeBlogPosts = storeDetailUiState.blogPosts,
            onBack = onBack
        )
    }
}

@Composable
private fun HomeDetailContent(
    storeDetailModel: StoreDetailModel,
    storeBlogPosts: List<BlogPostModel>,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    CakkAppbarWithBack(
        title = stringResource(R.string.home_detail_app_bar),
        onClick = onBack
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        HomeDetailHeader(
            modifier = Modifier.fillMaxWidth(),
            storeName = storeDetailModel.name,
            storeLocation = storeDetailModel.location,
            storeLink = storeDetailModel.link,
            storeThumbnail = storeDetailModel.thumbnail
        )

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

@Composable
private fun HomeDetailHeader(
    modifier: Modifier = Modifier,
    storeName: String,
    storeLocation: String,
    storeLink: String,
    storeThumbnail: String? = null
) {
    Column(
        modifier = modifier
            .background(Palatinate_Blue.copy(alpha = 0.1f))
            .padding(top = 30.dp, bottom = 24.dp)
    ) {
        HomeDetailHeaderInfo(
            modifier = Modifier.fillMaxWidth(),
            storeName = storeName,
            storeLocation = storeLocation,
            storeThumbnail = storeThumbnail
        )

        HomeDetailHeaderTab(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            storeLink = storeLink
        )
    }
}

@Composable
private fun HomeDetailHeaderInfo(
    modifier: Modifier = Modifier,
    storeName: String,
    storeLocation: String,
    storeThumbnail: String? = null
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = storeThumbnail,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 20.dp)
                .size(72.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.img_default)
        )

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = storeName,
                color = Raisin_Black,
                fontSize = 18.dp.toSp(),
                fontWeight = FontWeight.Bold,
                fontFamily = pretendard
            )

            Text(
                text = storeLocation,
                color = Raisin_Black.copy(alpha = 0.6f),
                fontSize = 14.dp.toSp(),
                fontWeight = FontWeight.Normal,
                fontFamily = pretendard
            )
        }
    }
}

@Composable
private fun HomeDetailHeaderTab(
    modifier: Modifier = Modifier,
    storeLink: String
) {
    Row(modifier) {
        HomeDetailHeaderTabItem(
            iconRes = if (storeLink.isEmpty()) {
                R.drawable.ic_kakao
            } else if (storeLink.contains("instagram")) {
                R.drawable.ic_instagram
            } else {
                R.drawable.ic_website
            },
            stringRes = if (storeLink.isEmpty()) {
                R.string.home_detail_kakao_tab
            } else if (storeLink.contains("instagram")) {
                R.string.home_detail_instagram_tab
            } else {
                R.string.home_detail_website_tab
            }
        )

        Spacer(
            modifier = Modifier
                .size(1.dp, 54.dp)
                .background(Raisin_Black.copy(alpha = 0.05f))
        )

        HomeDetailHeaderTabItem(
            iconRes = R.drawable.ic_heart,
            stringRes = R.string.home_detail_bookmark
        )

        Spacer(
            modifier = Modifier
                .size(1.dp, 54.dp)
                .background(Raisin_Black.copy(alpha = 0.05f))
        )

        HomeDetailHeaderTabItem(
            iconRes = R.drawable.ic_navigation,
            stringRes = R.string.home_detail_navigation
        )

        Spacer(
            modifier = Modifier
                .size(1.dp, 54.dp)
                .background(Raisin_Black.copy(alpha = 0.05f))
        )

        HomeDetailHeaderTabItem(
            iconRes = R.drawable.ic_share,
            stringRes = R.string.home_detail_share
        )
    }
}

@Composable
private fun RowScope.HomeDetailHeaderTabItem(
    @DrawableRes iconRes: Int,
    @StringRes stringRes: Int,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = stringResource(stringRes),
            color = Raisin_Black.copy(alpha = 0.8f),
            fontSize = 12.dp.toSp(),
            fontWeight = FontWeight.Normal,
            fontFamily = pretendard
        )
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
                        .background(
                            it
                                .toColor()
                                .copy(alpha = 0.2f),
                            RoundedCornerShape(14.dp)
                        )
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
