package org.prography.feed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.prography.designsystem.component.CakkAppbar
import org.prography.designsystem.R

@Composable
fun FeedScreen() {
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
                .fillMaxSize()
        )
    }
}

@Composable
private fun FeedContent(
    modifier: Modifier
) {
}
