package org.prography.designsystem.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import org.prography.designsystem.R
import org.prography.designsystem.ui.theme.Raisin_Black
import org.prography.designsystem.ui.theme.pretendard
import org.prography.utility.extensions.toSp

@Composable
fun CakkAppbarWithBack(
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp)
            .heightIn(max = 42.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(R.drawable.ic_left_arrow),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(18.dp, 24.dp),
                tint = Color.Unspecified
            )
        }

        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            color = Raisin_Black,
            fontSize = 17.dp.toSp(),
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CakkAppbar(
    modifier: Modifier = Modifier,
    title: String
) {
    Row(
        modifier = modifier.padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Raisin_Black,
            fontSize = 20.dp.toSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = pretendard
        )
    }
}

@Composable
fun CakkAppbarWithClose(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Raisin_Black,
            fontSize = 17.dp.toSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = pretendard,
            letterSpacing = (-0.04).em
        )
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 13.dp)
                .size(21.dp, 23.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                tint = Raisin_Black
            )
        }
    }
}
