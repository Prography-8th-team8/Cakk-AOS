package org.prography.home.detail

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.Html.FROM_HTML_OPTION_USE_CSS_COLORS
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.prography.designsystem.R
import org.prography.designsystem.component.CakkAppbarWithBack
import org.prography.designsystem.component.EmptyStoreItemTag
import org.prography.designsystem.component.StoreItemTag
import org.prography.designsystem.component.StoreItemTagRow
import org.prography.designsystem.ui.theme.Black
import org.prography.designsystem.ui.theme.Light_Deep_Pink
import org.prography.designsystem.ui.theme.Palatinate_Blue
import org.prography.designsystem.ui.theme.Platinum
import org.prography.designsystem.ui.theme.Raisin_Black
import org.prography.designsystem.ui.theme.White
import org.prography.designsystem.ui.theme.pretendard
import org.prography.domain.model.store.BlogPostModel
import org.prography.domain.model.store.StoreDetailModel
import org.prography.utility.extensions.toSp

private enum class TabType(val label: String) {
    IMAGES("케이크 이미지"), BLOG_REVIES("블로그 리뷰")
}

@Composable
fun HomeDetailScreen(
    homeDetailViewModel: HomeDetailViewModel = hiltViewModel(),
    storeId: Int,
    fromHome: Boolean = false,
    onBack: () -> Unit = {}
) {
    LaunchedEffect(storeId) {
        homeDetailViewModel.fetchDetailStore(storeId)
        homeDetailViewModel.fetchStoreBlogInfos(storeId)
    }

    val storeDetailUiState by homeDetailViewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CakkAppbarWithBack(
                title = stringResource(R.string.home_detail_app_bar),
                onClick = onBack
            )
        }
    ) { paddingValues ->
        HomeDetailContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(White),
            fromHome = fromHome,
            storeDetailModel = storeDetailUiState.storeDetailModel,
            storeBlogPosts = storeDetailUiState.blogPosts,
            showBlogPostCount = storeDetailUiState.showBlogPostCount,
            onChangeBlogPostCount = { homeDetailViewModel.changeShowBlogCount(it) },
            storeLatitude = storeDetailUiState.storeDetailModel.latitude,
            storeLongitude = storeDetailUiState.storeDetailModel.longitude
        )
    }
}

@Composable
private fun HomeDetailContent(
    modifier: Modifier = Modifier,
    fromHome: Boolean = false,
    storeDetailModel: StoreDetailModel,
    storeBlogPosts: List<BlogPostModel>,
    showBlogPostCount: Int,
    onChangeBlogPostCount: (Int) -> Unit = {},
    storeLatitude: Double,
    storeLongitude: Double
) {
    var isVisibleDialog by remember { mutableStateOf(false) }
    var tabType by remember { mutableStateOf(TabType.IMAGES) }
    val context = LocalContext.current

    if (isVisibleDialog) {
        Dialog(onDismissRequest = { }) {
            Surface(
                modifier = Modifier.size(328.dp, 166.dp),
                shape = RoundedCornerShape(14.dp),
                color = White
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.home_detail_find_naver_map),
                        modifier = Modifier.padding(top = 48.dp),
                        color = Raisin_Black,
                        fontSize = 18.dp.toSp(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = pretendard
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 36.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(144.dp, 44.dp)
                                .background(Raisin_Black.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .clickable { isVisibleDialog = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.dialog_cancel),
                                color = Raisin_Black.copy(alpha = 0.6f),
                                fontSize = 16.dp.toSp(),
                                fontWeight = FontWeight.Normal,
                                fontFamily = pretendard
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(144.dp, 44.dp)
                                .background(Light_Deep_Pink, RoundedCornerShape(12.dp))
                                .clickable {
                                    val naverMapUri = Uri
                                        .Builder()
                                        .scheme("nmap")
                                        .authority("route")
                                        .appendPath("public")
                                        .appendQueryParameter("dlat", storeLatitude.toString())
                                        .appendQueryParameter("dlng", storeLongitude.toString())
                                        .appendQueryParameter("dname", storeDetailModel.name)
                                        .appendQueryParameter("appname", "com.prography.cakk")
                                        .build()

                                    context.startActivity(Intent(Intent.ACTION_VIEW, naverMapUri))
                                    isVisibleDialog = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.dialog_confirm),
                                color = White,
                                fontSize = 16.dp.toSp(),
                                fontWeight = FontWeight.Normal,
                                fontFamily = pretendard
                            )
                        }
                    }
                }
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        userScrollEnabled = fromHome.not()
    ) {
        item(span = { GridItemSpan(3) }) {
            Column {
                HomeDetailHeader(
                    modifier = Modifier.fillMaxWidth(),
                    storeName = storeDetailModel.name,
                    storeLocation = storeDetailModel.location,
                    storeLink = storeDetailModel.link,
                    storeThumbnail = storeDetailModel.thumbnail,
                    onChangeDialogState = { isVisibleDialog = isVisibleDialog.not() }
                )
                HomeDetailKeywordRow(
                    modifier = Modifier.fillMaxWidth(),
                    storeTypes = storeDetailModel.storeTypes
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Raisin_Black.copy(alpha = 0.05f))
                )
                HomeDetailTabRow(
                    modifier = Modifier.fillMaxWidth(),
                    currentType = tabType,
                    onChangeType = { tabType = it }
                )
            }
        }

        when (tabType) {
            TabType.IMAGES -> showGridImage(storeDetailModel.imageUrls)
            TabType.BLOG_REVIES -> showBlogReviews(
                storeBlogPosts = storeBlogPosts,
                showBlogPostCount = showBlogPostCount,
                onChangeBlogPostCount = onChangeBlogPostCount
            )
        }
    }
}

private fun LazyGridScope.showBlogReviews(
    storeBlogPosts: List<BlogPostModel>,
    showBlogPostCount: Int,
    onChangeBlogPostCount: (Int) -> Unit = {}
) {
    item(span = { GridItemSpan(3) }) {
        HomeDetailBlogRow(
            modifier = Modifier.fillMaxWidth(),
            blogPosts = storeBlogPosts,
            showBlogPostCount = showBlogPostCount,
            onChangeBlogPostCount = onChangeBlogPostCount
        )
    }
}

@Composable
private fun HomeDetailTabRow(
    modifier: Modifier = Modifier,
    currentType: TabType,
    onChangeType: (TabType) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .height(54.dp)
                .clickable { onChangeType(TabType.IMAGES) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = TabType.IMAGES.label,
                    color = Black,
                    fontSize = 16.dp.toSp(),
                    fontWeight = if (currentType == TabType.IMAGES) FontWeight.Bold else FontWeight.Normal,
                    fontFamily = pretendard
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        if (currentType == TabType.IMAGES) Raisin_Black else Raisin_Black.copy(0.2f)
                    )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(54.dp)
                .clickable { onChangeType(TabType.BLOG_REVIES) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = TabType.BLOG_REVIES.label,
                    color = Black,
                    fontSize = 16.dp.toSp(),
                    fontWeight = if (currentType == TabType.BLOG_REVIES) FontWeight.Bold else FontWeight.Normal,
                    fontFamily = pretendard
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        if (currentType == TabType.BLOG_REVIES) Raisin_Black else Raisin_Black.copy(0.2f)
                    )
            )
        }
    }
}

private fun LazyGridScope.showGridImage(
    imageUrls: List<String>
) {
    if (imageUrls.isEmpty()) {
        item(span = { GridItemSpan(3) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 95.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.img_empty_cakeshop),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = stringResource(R.string.home_detail_empty_cakeshop_image),
                    modifier = Modifier.padding(top = 20.dp),
                    color = Raisin_Black,
                    fontSize = 16.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendard
                )
                Text(
                    text = stringResource(R.string.home_detail_empty_cakeshop_guide_message),
                    modifier = Modifier.padding(top = 10.dp),
                    color = Raisin_Black.copy(alpha = 0.6f),
                    fontSize = 14.dp.toSp(),
                    fontWeight = FontWeight.Normal,
                    fontFamily = pretendard
                )
            }
        }
    } else {
        itemsIndexed(imageUrls) { index, imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = imageUrl,
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .padding(end = if (index % 3 != 2) 6.dp else 0.dp)
                    .height(116.dp),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
private fun HomeDetailKeywordRow(
    modifier: Modifier = Modifier,
    storeTypes: List<String>
) {
    Column(
        modifier = modifier
            .padding(vertical = 24.dp)
            .padding(start = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.home_detail_keyword),
            color = Raisin_Black,
            fontSize = 18.dp.toSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = pretendard
        )
        StoreItemTagRow(
            storeTypes = storeTypes,
            emptyContent = {
                EmptyStoreItemTag(Modifier.padding(top = 16.dp))
            },
            content = {
                StoreItemTag(
                    modifier = Modifier.padding(top = 16.dp),
                    storeTypes = storeTypes
                )
            }
        )
    }
}

@Composable
private fun HomeDetailHeader(
    modifier: Modifier = Modifier,
    storeName: String,
    storeLocation: String,
    storeLink: String,
    storeThumbnail: String? = null,
    onChangeDialogState: () -> Unit
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
            storeName = storeName,
            storeLocation = storeLocation,
            storeLink = storeLink,
            onChangeDialogState = onChangeDialogState
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
    storeName: String,
    storeLocation: String,
    storeLink: String,
    onChangeDialogState: () -> Unit
) {
    val context = LocalContext.current
    Row(modifier) {
        HomeDetailHeaderTabItem(
            iconRes = when {
                storeLink.contains(stringResource(R.string.infix_kakao_url)) -> R.drawable.ic_kakao
                storeLink.contains(stringResource(R.string.infix_instagram_url)) -> R.drawable.ic_instagram
                else -> R.drawable.ic_website
            },
            stringRes = when {
                storeLink.contains(stringResource(R.string.infix_kakao_url)) -> R.string.home_detail_kakao_tab
                storeLink.contains(stringResource(R.string.infix_instagram_url)) -> R.string.home_detail_instagram_tab
                else -> R.string.home_detail_website_tab
            }
        ) {
            if (storeLink.isNotEmpty()) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(storeLink)))
            }
        }

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
            stringRes = R.string.home_detail_navigation,
            onClick = onChangeDialogState
        )

        Spacer(
            modifier = Modifier
                .size(1.dp, 54.dp)
                .background(Raisin_Black.copy(alpha = 0.05f))
        )

        HomeDetailHeaderTabItem(
            iconRes = R.drawable.ic_share,
            stringRes = R.string.home_detail_share
        ) {
            if (storeLink.isNotEmpty()) {
                context.startActivity(
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "$storeName\n$storeLocation\n$storeLink")
                    }
                )
            }
        }
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
    showBlogPostCount: Int,
    onChangeBlogPostCount: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        blogPosts.take(showBlogPostCount).forEachIndexed { index, blogPost ->
            HomeDetailBlogItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(bottom = 24.dp),
                blogPost = blogPost
            )

            if (index < showBlogPostCount - 1) {
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Platinum)
                )
            }
        }

        if (showBlogPostCount == DEFAULT_BLOG_POST_COUNT && blogPosts.size > DEFAULT_BLOG_POST_COUNT) {
            Button(
                onClick = { onChangeBlogPostCount(blogPosts.size) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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
        modifier = modifier
            .clickable {
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
