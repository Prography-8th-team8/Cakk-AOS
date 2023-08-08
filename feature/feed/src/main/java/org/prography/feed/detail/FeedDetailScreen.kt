package org.prography.feed.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.prography.designsystem.component.CakkAppbarWithClose

@Composable
fun FeedDetailScreen(
    storeId: Int,
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            CakkAppbarWithClose(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                title = "프롬데이지",
                onClick = onClose
            )
        }
    ) { paddingValues ->
        FeedDetailContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

@Composable
private fun FeedDetailContent(
    modifier: Modifier = Modifier
) {
}
