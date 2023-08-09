package org.prography.feed.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import org.prography.designsystem.R
import org.prography.designsystem.component.CakkAppbarWithClose
import org.prography.designsystem.ui.theme.Light_Deep_Pink
import org.prography.designsystem.ui.theme.Raisin_Black
import org.prography.designsystem.ui.theme.White
import org.prography.designsystem.ui.theme.pretendard
import org.prography.utility.extensions.toSp

@Composable
fun FeedDetailScreen(
    feedDetailViewModel: FeedDetailViewModel = hiltViewModel(),
    storeId: Int,
    onClose: () -> Unit
) {
    LaunchedEffect(storeId) {
        feedDetailViewModel.fetchStoreDetailInfo(storeId)
    }

    val feedDetailUiState by feedDetailViewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CakkAppbarWithClose(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                title = feedDetailUiState.storeDetailModel.name,
                onClick = onClose
            )
        }
    ) { paddingValues ->
        FeedDetailContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            storeLocation = feedDetailUiState.storeDetailModel.location,
            storeImageUrls = feedDetailUiState.storeDetailModel.imageUrls
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FeedDetailContent(
    modifier: Modifier = Modifier,
    storeLocation: String,
    storeImageUrls: List<String>
) {
    val pagerState = rememberPagerState { storeImageUrls.size }
    val scope = rememberCoroutineScope()
    Box(modifier) {
        Image(
            painter = painterResource(R.drawable.img_feed_detail),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.1f
        )
        if (storeLocation.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 20.dp, end = 16.dp)
                    .background(Raisin_Black.copy(alpha = 0.4f), RoundedCornerShape(38.dp))
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = storeLocation.split(' ')[1],
                    color = White,
                    fontSize = 12.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendard
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-44).dp)
                .fillMaxWidth()
        ) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = storeImageUrls[index],
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                IconButton(
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                        .size(38.dp)
                        .background(White.copy(0.8f), CircleShape)
                        .border(1.dp, Raisin_Black.copy(0.1f), CircleShape),
                    enabled = pagerState.currentPage > 0
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Raisin_Black.copy(0.6f)
                    )
                }
                IconButton(
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                        .size(38.dp)
                        .background(White.copy(0.8f), CircleShape)
                        .border(1.dp, Raisin_Black.copy(0.1f), CircleShape),
                    enabled = pagerState.currentPage < storeImageUrls.lastIndex
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Raisin_Black.copy(0.6f)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(260.dp, 56.dp)
                    .background(Light_Deep_Pink, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.feed_detail_visit_cakeshop),
                    color = White,
                    fontSize = 18.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendard
                )
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(56.dp)
                    .border(1.dp, Raisin_Black.copy(0.1f), RoundedCornerShape(12.dp))
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Raisin_Black.copy(0.4f)
                )
            }
        }
    }
}
