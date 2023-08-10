package org.prography.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.prography.designsystem.ui.theme.Raisin_Black
import org.prography.designsystem.ui.theme.White
import org.prography.designsystem.ui.theme.pretendard
import org.prography.utility.extensions.toSp

@Composable
fun CakkSnackbarHost(
    snackbarHostState: SnackbarHostState,
    content: @Composable BoxScope.(SnackbarData) -> Unit
) {
    SnackbarHost(
        hostState = snackbarHostState
    ) { snackbarData ->
        Box(Modifier.fillMaxSize()) {
            content(snackbarData)
        }
    }
}

@Composable
fun BoxScope.CakkSnackbar(
    modifier: Modifier = Modifier,
    snackbarData: SnackbarData
) {
    Box(
        modifier = modifier
            .padding(vertical = 24.dp)
            .fillMaxWidth()
            .height(46.dp)
            .padding(horizontal = 17.dp)
            .background(Raisin_Black.copy(0.8f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = snackbarData.message,
            color = White,
            fontSize = 15.dp.toSp(),
            fontWeight = FontWeight.Normal,
            fontFamily = pretendard
        )
    }
}
