package org.prography.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.prography.designsystem.R
import org.prography.designsystem.component.StoreItemContent
import org.prography.designsystem.ui.theme.Raisin_Black
import org.prography.designsystem.ui.theme.pretendard

@Composable
fun MyScreen(
    myViewModel: MyViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit
) {
    LaunchedEffect(myViewModel.state) {
        myViewModel.fetchBookmarkedList()
    }

    val myUiState by myViewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(top = 53.dp)) {
        MyPage()
        BookmarkCakeShop()
        if (myUiState.bookmarkModels.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(top = 20.dp),
            ) {
                items(myUiState.bookmarkModels) { bookmarkedStore ->
                    StoreItemContent(
                        modifier = Modifier.fillMaxWidth(),
                        storeModel = bookmarkedStore,
                        isFavorite = bookmarkedStore.bookmarked,
                        isHomeScreen = false,
                        bookmark = { myViewModel.bookmarkCakeShop(bookmarkedStore) },
                        unBookmark = { myViewModel.unBookmarkCakeShop(bookmarkedStore.id) },
                        onClick = { onNavigateToDetail(bookmarkedStore.id) }
                    )
                }
            }
        } else {
            EmptyBookmark()
        }
    }
}

@Composable
private fun BookmarkCakeShop() {
    Text(
        text = stringResource(id = R.string.bookmark_bookmarked_cakk_shop),
        modifier = Modifier.padding(start = 20.dp),
        color = Raisin_Black.copy(alpha = 0.8f),
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = pretendard
    )
}

@Composable
private fun MyPage() {
    Text(
        text = stringResource(id = R.string.bookmark_my_page),
        modifier = Modifier.padding(start = 20.dp),
        color = Raisin_Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = pretendard
    )
    Divider(
        modifier = Modifier.padding(top = 10.dp, bottom = 25.dp),
        thickness = 1.dp,
        color = Raisin_Black.copy(alpha = 0.05f)
    )
}

@Composable
fun EmptyBookmark() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_empty_bookmark_cakeshop),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.bookmark_empty_bookmark_shop),
            color = Raisin_Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = pretendard
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.bookmark_try_bookmark),
            color = Raisin_Black.copy(alpha = 0.6f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = pretendard
        )
    }
}
