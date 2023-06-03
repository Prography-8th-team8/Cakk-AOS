package org.prography.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import org.prography.cakk.data.api.model.response.StoreListResponse
import org.prography.designsystem.R
import org.prography.designsystem.ui.theme.*
import org.prography.enums.DistrictType
import org.prography.utility.extensions.toSp

enum class ExpandedType {
    HALF, FULL, COLLAPSED, MOVING
}

@SuppressLint("InternalInsetResource", "DiscouragedApi")
@Composable
fun HomeScreen(
    navHostController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val storeList by homeViewModel.stores.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val statusBarHeight = LocalContext.current.resources.getDimensionPixelSize(
        LocalContext.current.resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android",
        ),
    )
    BottomSheet(storeList, screenHeight, statusBarHeight)
}

@SuppressLint("InternalInsetResource", "DiscouragedApi")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheet(storeList: List<StoreListResponse>, screenHeight: Int, statusBarHeight: Int) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Expanded),
    )
    var offsetY by remember { mutableStateOf((screenHeight / 20).toFloat()) }
    var expandedType by remember {
        mutableStateOf(ExpandedType.COLLAPSED)
    }
    val height by animateIntAsState(
        when (expandedType) {
            ExpandedType.HALF -> {
                offsetY = (screenHeight / 2.5).toFloat()
                (screenHeight / 2.5).toInt()
            }
            ExpandedType.FULL -> {
                offsetY = screenHeight.toFloat()
                screenHeight - statusBarHeight
            }
            ExpandedType.COLLAPSED -> 53
            ExpandedType.MOVING -> offsetY.toInt()
        },
    )
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetElevation = 0.dp,
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
        ),
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(height.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                expandedType = ExpandedType.MOVING
                                offsetY -= dragAmount.y
                            },
                            onDragEnd = {
                                expandedType = when {
                                    offsetY >= (screenHeight / 1.5).toFloat() -> {
                                        ExpandedType.FULL
                                    }
                                    offsetY >= (screenHeight / 4).toFloat() && offsetY < (screenHeight / 1.5).toFloat() -> {
                                        ExpandedType.HALF
                                    }
                                    else -> {
                                        offsetY = 53f
                                        ExpandedType.COLLAPSED
                                    }
                                }
                            },
                        )
                    }
                    .background(White),
            ) {
                BottomSheetContent(storeList)
            }
        },
        sheetPeekHeight = height.dp,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CakkMap(storeList)
        }
    }
}

@Composable
private fun BottomSheetContent(storeList: List<StoreListResponse>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BottomSheetTop(modifier = Modifier.align(Alignment.Start))

        LazyColumn(
            modifier = Modifier.padding(top = 22.dp),
        ) {
            items(storeList) { store ->
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = OldLace
                ) {
                    Column {
                        StoreInfo(store)
                        Spacer(modifier = Modifier.height(48.dp))
                        StoreTags(store)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun StoreInfo(store: StoreListResponse) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp)
    ) {
        Text(
            text = store.name,
            color = Raisin_Black,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 18.dp.toSp(),
        )
        Divider(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(width = 1.dp, height = 12.dp)
        )
        Text(
            text = DistrictType.valueOf(store.district).districtKr,
            color = Raisin_Black,
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 16.dp.toSp(),
        )
    }
    Text(
        text = store.location,
        color = Raisin_Black.copy(alpha = 0.6f),
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.dp.toSp(),
        modifier = Modifier.padding(start = 20.dp, top = 12.dp)
    )
}

@Composable
private fun StoreTags(store: StoreListResponse) {
    Row(modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)) {
        store.storeTypes.forEach { storeType ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = StoreType.valueOf(storeType).color.copy(alpha = 0.2f)
            ) {
                Text(
                    text = StoreType.valueOf(storeType).tag,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                    color = StoreType.valueOf(storeType).color,
                    fontSize = 12.sp,
                    fontFamily = pretendard
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (store.storeTypes.size - 3 > 0) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Black
            ) {
                Text(
                    text = "+${store.storeTypes.size - 3}",
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                    color = White,
                    fontSize = 12.sp,
                    fontFamily = pretendard
                )
            }
        }
    }
}

@Composable
private fun BottomSheetTop(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_line),
        contentDescription = null,
        modifier = Modifier.padding(top = 20.dp)
    )
    Text(
        text = "은평, 마포, 서대문",
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 18.dp.toSp(),
        color = Raisin_Black,
        modifier = modifier
            .padding(start = 20.dp, top = 30.dp)
    )
    Text(
        text = "24개의 케이크샵",
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.dp.toSp(),
        color = Raisin_Black.copy(alpha = 0.8f),
        modifier = modifier
            .padding(start = 20.dp, top = 12.dp)
    )
}

@Composable
@OptIn(ExperimentalNaverMapApi::class)
private fun CakkMap(storeList: List<StoreListResponse>) {
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                isLocationButtonEnabled = false,
                isCompassEnabled = false,
                isScaleBarEnabled = false,
                isZoomControlEnabled = false,
            )
        )
    }

    NaverMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = mapUiSettings
    ) {
        storeList.forEach { store ->
            Marker(
                state = MarkerState(position = LatLng(store.latitude, store.longitude)),
                icon = OverlayImage.fromResource(R.drawable.ic_marker),
            )
        }
    }
}
