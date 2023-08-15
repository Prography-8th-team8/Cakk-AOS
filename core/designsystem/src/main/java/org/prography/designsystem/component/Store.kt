package org.prography.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.prography.designsystem.R
import org.prography.designsystem.mapper.toColor
import org.prography.designsystem.ui.theme.*
import org.prography.domain.model.enums.DistrictType
import org.prography.domain.model.enums.StoreType
import org.prography.domain.model.store.StoreModel
import org.prography.utility.extensions.toSp

@Composable
fun StoreItemContent(
    modifier: Modifier = Modifier,
    storeModel: StoreModel = StoreModel(),
    isFavorite: Boolean = false,
    isHomeScreen: Boolean = true,
    bookmark: () -> Unit = {},
    unBookmark: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = Old_Lace,
        border = BorderStroke(1.dp, Raisin_Black.copy(0.1f))
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .padding(start = 20.dp)
        ) {
            StoreContent(storeModel, bookmark, unBookmark, isFavorite, isHomeScreen)
        }
    }
}

@Composable
private fun StoreContent(
    storeModel: StoreModel,
    bookmark: () -> Unit,
    unBookmark: () -> Unit,
    isFavorite: Boolean,
    isHomeScreen: Boolean
) {
    StoreItemHeader(
        storeName = storeModel.name,
        storeDistrict = storeModel.district,
        storeLocation = storeModel.location,
        bookmark = bookmark,
        unBookmark = unBookmark,
        isFavorite = isFavorite,
    )

    if (isHomeScreen) {
        StoreItemTagRow(
            storeTypes = storeModel.storeTypes,
            emptyContent = {
                EmptyStoreItemTag(Modifier.padding(top = 12.dp))
            },
            content = {
                StoreItemTag(
                    modifier = Modifier.padding(top = 12.dp),
                    storeTypes = storeModel.storeTypes,
                    maxCount = 3
                ) { size ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Black
                    ) {
                        Text(
                            text = String.format(stringResource(R.string.home_num_of_keyword), size),
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                            color = White,
                            fontSize = 12.dp.toSp(),
                            fontWeight = FontWeight.Bold,
                            fontFamily = pretendard
                        )
                    }
                }
            }
        )
    }

    StoreItemImageRow(
        storeImageUrls = storeModel.imageUrls,
        emptyContent = {
            EmptyStoreItemImage(Modifier.padding(top = 32.dp))
        },
        content = {
            StoreItemImage(
                modifier = Modifier.padding(top = 32.dp),
                storeImageUrls = storeModel.imageUrls
            )
        }
    )
}

@Composable
internal fun StoreItemImageRow(
    storeImageUrls: List<String> = listOf(),
    emptyContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    if (storeImageUrls.isEmpty()) emptyContent() else content()
}

@Composable
private fun EmptyStoreItemImage(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.img_no_photo),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 20.dp)
            .aspectRatio(320 / 96f)
    )
}

@Composable
private fun StoreItemImage(
    modifier: Modifier = Modifier,
    storeImageUrls: List<String> = listOf()
) {
    LazyRow(modifier = modifier) {
        items(storeImageUrls, key = { it }) { storeImageUrl ->
            AsyncImage(
                model = storeImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(96.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
    }
}

@Composable
fun StoreItemTagRow(
    storeTypes: List<String> = listOf(),
    emptyContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    if (storeTypes.isEmpty()) {
        emptyContent()
    } else {
        content()
    }
}

@Composable
fun StoreItemTag(
    modifier: Modifier = Modifier,
    storeTypes: List<String> = listOf(),
    maxCount: Int = storeTypes.size,
    overFlow: @Composable (Int) -> Unit = {}
) {
    LazyRow(modifier) {
        items(storeTypes.take(maxCount), key = { it }) { storeType ->
            Surface(
                modifier = Modifier.padding(end = 4.dp),
                shape = RoundedCornerShape(14.dp),
                color = StoreType.valueOf(storeType).toColor().copy(alpha = 0.2f)
            ) {
                Text(
                    text = StoreType.valueOf(storeType).tag,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                    color = StoreType.valueOf(storeType).toColor(),
                    fontSize = 12.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendard
                )
            }
        }

        if (storeTypes.size > maxCount) {
            item { overFlow(storeTypes.size - maxCount) }
        }
    }
}

@Composable
fun EmptyStoreItemTag(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Raisin_Black.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
            .padding(vertical = 7.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_alert),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color.Unspecified
        )
        Text(
            text = stringResource(R.string.home_detail_empty_keyword),
            modifier = Modifier.padding(start = 4.dp),
            color = White,
            fontSize = 12.dp.toSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = pretendard
        )
    }
}

@Composable
internal fun StoreItemHeader(
    modifier: Modifier = Modifier,
    storeName: String,
    storeDistrict: String,
    storeLocation: String,
    bookmark: () -> Unit,
    unBookmark: () -> Unit,
    isFavorite: Boolean = false
) {
    Row(modifier) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = storeName,
                    color = Raisin_Black,
                    fontSize = 16.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendard
                )
                Divider(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(width = 1.dp, height = 12.5.dp)
                        .background(Raisin_Black.copy(alpha = 0.4f))
                )
                Text(
                    text = String.format(stringResource(R.string.home_district_name), DistrictType.valueOf(storeDistrict).districtKr),
                    color = Raisin_Black,
                    fontSize = 12.dp.toSp(),
                    fontWeight = FontWeight.Normal,
                    fontFamily = pretendard
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Text(
                text = storeLocation,
                modifier = Modifier.padding(top = 4.dp),
                color = Raisin_Black.copy(alpha = 0.6f),
                fontSize = 12.dp.toSp(),
                fontWeight = FontWeight.Normal,
                fontFamily = pretendard
            )
        }

        IconButton(
            onClick = {
                if (isFavorite.not()) {
                    bookmark()
                } else {
                    unBookmark()
                }
            },
            modifier = Modifier
                .padding(end = 20.dp)
                .size(24.dp)
        ) {
            Icon(
                painter = painterResource(
                    if (isFavorite) {
                        R.drawable.ic_heart_sel
                    } else {
                        R.drawable.ic_heart
                    }
                ),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}
