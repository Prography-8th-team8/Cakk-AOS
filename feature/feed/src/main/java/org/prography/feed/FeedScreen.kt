package org.prography.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import org.prography.designsystem.component.CakkAppbar
import org.prography.designsystem.R

@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel = hiltViewModel(),
    onNavigateFeedDetail: () -> Unit
) {
    Scaffold(
        topBar = {
            CakkAppbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                title = stringResource(R.string.feed_app_bar)
            )
        }
    ) { paddingValues ->
        FeedContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            feedViewModel = feedViewModel,
            onNavigateFeedDetail = onNavigateFeedDetail
        )
    }
}

@Composable
private fun FeedContent(
    modifier: Modifier = Modifier,
    feedViewModel: FeedViewModel,
    onNavigateFeedDetail: () -> Unit
) {
    val feedItems = feedViewModel.feedItems.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
    ) {
        items(count = feedItems.itemCount) { index ->
            val item = feedItems[index]
            item?.let { item ->
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.imageUrl,
                    modifier = Modifier
                        .size(116.dp)
                        .padding(top = 3.dp)
                        .padding(bottom = 3.dp)
                        .padding(horizontal = if (index % 3 == 1) 6.dp else 0.dp)
                        .clickable { onNavigateFeedDetail() },
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}
