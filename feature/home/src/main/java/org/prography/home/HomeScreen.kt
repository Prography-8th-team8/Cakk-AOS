package org.prography.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.prography.common.navigation.destination.CakkDestination.Home.DEFAULT_DISTRICTS_INFO
import org.prography.common.navigation.destination.CakkDestination.Home.DEFAULT_STORE_COUNT
import org.prography.designsystem.R
import org.prography.designsystem.component.CakkSnackbar
import org.prography.designsystem.component.CakkSnackbarHost
import org.prography.designsystem.component.StoreItemContent
import org.prography.designsystem.mapper.toBackgroundColor
import org.prography.designsystem.mapper.toIcon
import org.prography.designsystem.ui.theme.*
import org.prography.domain.model.enums.DistrictType
import org.prography.domain.model.enums.StoreType
import org.prography.domain.model.store.StoreModel
import org.prography.home.detail.HomeDetailScreen
import org.prography.utility.extensions.toSp
import timber.log.Timber

var locationCallback: LocationCallback? = null
var fusedLocationClient: FusedLocationProviderClient? = null

@SuppressLint("InternalInsetResource", "DiscouragedApi")
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    districtsArg: String,
    storeCountArg: Int,
    onNavigateToOnBoarding: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
) {
    val fromOnBoarding = districtsArg != DEFAULT_DISTRICTS_INFO && storeCountArg != DEFAULT_STORE_COUNT
    LocationPermission(
        fromOnBoarding = fromOnBoarding,
        onNavigateToOnBoarding = onNavigateToOnBoarding
    )

    BottomSheet(
        homeViewModel = homeViewModel,
        fromOnBoarding = fromOnBoarding,
        districtsArg = districtsArg,
        storeCountArg = storeCountArg,
        onNavigateToOnBoarding = onNavigateToOnBoarding,
        onNavigateToDetail = onNavigateToDetail
    )

    LaunchedEffect(homeViewModel) {
        if (fromOnBoarding) {
            homeViewModel.fetchStoreList(
                districts = districtsArg.split(" "),
                storeTypes = StoreType.values().joinToString(",") { it.name }
            )
        }
    }
}

@SuppressLint("InternalInsetResource", "DiscouragedApi")
@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
private fun BottomSheet(
    homeViewModel: HomeViewModel = hiltViewModel(),
    fromOnBoarding: Boolean,
    districtsArg: String,
    storeCountArg: Int,
    onNavigateToOnBoarding: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
) {
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val statusBarHeight = context.resources.getDimensionPixelSize(
        context.resources.getIdentifier(
            stringResource(id = R.string.home_status_bar_height),
            stringResource(id = R.string.home_dimen),
            stringResource(id = R.string.home_android),
        ),
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Expanded),
    )
    val homeUiState by homeViewModel.state.collectAsStateWithLifecycle()
    var isFullDetailScreen = false
    var offsetY by rememberSaveable { mutableStateOf(((screenHeight / 2.5).toInt()).dp.value) }
    var expandedType by rememberSaveable { mutableStateOf(homeUiState.expandedType) }
    val height by animateDpAsState(expandedType.getByScreenHeight(expandedType, screenHeight, statusBarHeight, offsetY))
    val modifier = if (homeUiState.bottomSheetType != BottomSheetType.Filter) Modifier.height(height) else Modifier.wrapContentHeight()

    LaunchedEffect(homeViewModel) {
        homeViewModel.sideEffect.collectLatest {
            when (it) {
                HomeSideEffect.ReloadError -> {
                    bottomSheetScaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.snackbar_not_provide_service)
                    )
                }
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetElevation = 0.dp,
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
        ),
        snackbarHost = { snackbarHostState ->
            CakkSnackbarHost(snackbarHostState = snackbarHostState) { snackbarData ->
                CakkSnackbar(
                    snackbarData = snackbarData,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        },
        sheetContent = {
            Box(
                modifier
                    .fillMaxWidth()
                    .background(White)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                expandedType = ExpandedType.MOVING
                            },
                            onDrag = { change, dragAmount ->
                                if (offsetY - (dragAmount.y * 0.5).toFloat() <= screenHeight) {
                                    change.consume()
                                    offsetY -= (dragAmount.y * 0.5).toFloat()
                                } else {
                                    if ((homeUiState.bottomSheetType is BottomSheetType.StoreDetail) && isFullDetailScreen.not()) {
                                        isFullDetailScreen = true
                                        onNavigateToDetail((homeUiState.bottomSheetType as BottomSheetType.StoreDetail).storeId)
                                        return@detectDragGestures
                                    }
                                }
                            },
                            onDragEnd = {
                                expandedType = when {
                                    offsetY >= (screenHeight / 1.5).toFloat() -> {
                                        homeViewModel.changeBottomSheetState(ExpandedType.FULL)
                                        ExpandedType.FULL
                                    }

                                    offsetY >= (screenHeight / 4).toFloat() && offsetY < (screenHeight / 1.5).toFloat() -> {
                                        homeViewModel.changeBottomSheetState(ExpandedType.QUARTER)
                                        ExpandedType.QUARTER
                                    }

                                    else -> {
                                        homeViewModel.changeBottomSheetState(ExpandedType.COLLAPSED)
                                        ExpandedType.COLLAPSED
                                    }
                                }
                                offsetY = expandedType
                                    .getByScreenHeight(expandedType, screenHeight, statusBarHeight, offsetY)
                                    .value
                            },
                        )
                    }
            ) {
                when (homeUiState.bottomSheetType) {
                    BottomSheetType.StoreList -> {
                        CakeStoreContent(
                            isReload = homeUiState.isReload,
                            storeList = homeUiState.storeModels,
                            districts = if (districtsArg.isNotEmpty()) {
                                districtsArg.split(" ")
                                    .map { DistrictType.getName(it) }
                            } else {
                                listOf()
                            },
                            storeCount = if (storeCountArg >= 0 && homeUiState.isReload.not()) {
                                storeCountArg
                            } else {
                                homeUiState.storeModels.size
                            },
                            onNavigateToOnBoarding = onNavigateToOnBoarding,
                            onNavigateToDetail = onNavigateToDetail,
                            openFilterSheet = {
                                homeViewModel.changeBottomSheetType(BottomSheetType.Filter)
                                homeViewModel.changeBottomSheetState(ExpandedType.HALF)
                                expandedType = ExpandedType.HALF
                                offsetY = expandedType
                                    .getByScreenHeight(expandedType, screenHeight, statusBarHeight, offsetY)
                                    .value
                            },
                            onFavoriteClick = { homeViewModel.bookmarkCakeShop(it) },
                            onUnFavoriteClick = { homeViewModel.unBookmarkCakeShop(it) }
                        )
                    }

                    BottomSheetType.Filter -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            val selectFilter = remember {
                                mutableStateListOf(false, false, false, false, false, false, false, false, false, false)
                            }
                            val filters = remember { mutableStateOf("") }

                            Column {
                                FilterTopBar(selectFilter = selectFilter) {
                                    homeViewModel.changeBottomSheetType(BottomSheetType.StoreList)
                                    homeViewModel.changeBottomSheetState(ExpandedType.QUARTER)
                                    expandedType = ExpandedType.QUARTER
                                    offsetY = expandedType
                                        .getByScreenHeight(expandedType, screenHeight, statusBarHeight, offsetY)
                                        .value
                                }
                                Spacer(modifier = Modifier.height(21.dp))
                                Filters(selectFilter = selectFilter)
                            }
                            FilterSelectButton(
                                selectFilter,
                                filters,
                                homeViewModel,
                                districtsArg,
                            ) {
                                homeViewModel.changeBottomSheetType(BottomSheetType.StoreList)
                                homeViewModel.changeBottomSheetState(ExpandedType.QUARTER)
                                expandedType = ExpandedType.QUARTER
                                offsetY = expandedType
                                    .getByScreenHeight(expandedType, screenHeight, statusBarHeight, offsetY)
                                    .value
                            }
                        }
                    }

                    is BottomSheetType.StoreDetail -> {
                        HomeDetailScreen(
                            storeId = (homeUiState.bottomSheetType as BottomSheetType.StoreDetail).storeId,
                            fromHome = true,
                            onBack = { homeViewModel.changeBottomSheetType(BottomSheetType.StoreList) }
                        )
                    }
                }
            }
        },
        sheetPeekHeight = height,
    ) {
        val cameraPositionState: CameraPositionState =
            rememberCameraPositionState { position = CameraPosition(LatLng(37.566535, 126.9779692), 16.0) }
        Box(modifier = Modifier.fillMaxSize()) {
            CakkMap(
                homeViewModel = homeViewModel,
                cameraPositionState = cameraPositionState,
                bottomSheetType = homeUiState.bottomSheetType,
                fromOnBoarding = fromOnBoarding,
                isReload = homeUiState.isReload,
                storeList = homeUiState.storeModels
            )
            SearchArea(
                homeViewModel = homeViewModel,
                modifier = Modifier.align(Alignment.TopCenter),
                cameraPositionState = cameraPositionState
            )
        }
    }
}

@Composable
private fun FilterTopBar(
    selectFilter: SnapshotStateList<Boolean>,
    backToCakeStore: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Image(
            painterResource(R.drawable.ic_close),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 20.dp, end = 20.dp)
                .align(Alignment.End)
                .clickable { backToCakeStore() },
        )
        Spacer(modifier = Modifier.height(9.dp))
        Text(
            text = stringResource(id = R.string.home_filter),
            modifier = Modifier.padding(start = 20.dp),
            color = Raisin_Black,
            fontSize = 18.dp.toSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = pretendard,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.home_filter_recommend),
                modifier = Modifier.padding(start = 20.dp),
                color = Raisin_Black.copy(alpha = 0.6f),
                fontSize = 14.dp.toSp(),
                fontWeight = FontWeight.Normal,
                fontFamily = pretendard,
                letterSpacing = (-0.03).em
            )
            Spacer(modifier = Modifier.width(13.dp))
            Image(
                painterResource(id = R.drawable.ic_refresh),
                contentDescription = stringResource(id = R.string.home_content_description_filter_refresh_btn),
                modifier = Modifier.clickable {
                    selectFilter.indices.forEach { selectFilter[it] = false }
                }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun Filters(selectFilter: SnapshotStateList<Boolean>) {
    FlowRow(
        modifier = Modifier.padding(start = 16.dp, end = 18.dp)
    ) {
        StoreType.values().forEachIndexed { index, storeType ->
            Row(
                modifier = Modifier
                    .padding(end = 7.dp, bottom = 12.dp)
                    .border(BorderStroke(1.dp, Raisin_Black.copy(alpha = 0.1f)), RoundedCornerShape(50.dp))
                    .background(storeType.toBackgroundColor(selectFilter[index]), RoundedCornerShape(50.dp))
                    .toggleable(
                        value = selectFilter[index],
                        onValueChange = {
                            selectFilter[index] = !selectFilter[index]
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                storeType.toIcon()?.let { icon ->
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = storeType.tag,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(if (storeType.toIcon() == null) 12.dp else 4.dp))
                Text(
                    text = storeType.tag,
                    modifier = Modifier.padding(top = 10.dp, end = 12.dp, bottom = 10.dp),
                    color = Raisin_Black,
                    fontSize = 14.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendard,
                    letterSpacing = (-0.03).em
                )
            }
        }
    }
}

@Composable
private fun FilterSelectButton(
    selectFilter: SnapshotStateList<Boolean>,
    filters: MutableState<String>,
    homeViewModel: HomeViewModel,
    districtsArg: String,
    backToCakeStore: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 12.dp, end = 16.dp)
            .clickable(enabled = selectFilter.count { it } > 0) {
                StoreType.values().forEachIndexed { index, storeType ->
                    if (selectFilter[index]) filters.value += "${storeType.name},"
                }
                homeViewModel.saveStoreTypes(filters.value)
//                homeViewModel.fetchStoreList(
//                    districts = districtsArg.split(" "),
//                    storeTypes = filters.value
//                )
                homeViewModel.changeBottomSheetState(ExpandedType.QUARTER)
                backToCakeStore()
            },
        shape = RoundedCornerShape(12.dp),
        color = if (selectFilter.count { it } > 0) Light_Deep_Pink else Raisin_Black.copy(alpha = 0.2f),
    ) {
        Text(
            text = stringResource(id = R.string.home_apply),
            modifier = Modifier.padding(vertical = 19.dp),
            color = White,
            fontSize = 18.dp.toSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = pretendard,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SearchArea(
    homeViewModel: HomeViewModel,
    modifier: Modifier,
    cameraPositionState: CameraPositionState
) {
    var canReload by remember { mutableStateOf(false) }
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.isMoving }
            .distinctUntilChanged()
            .filter { it }
            .collect { canReload = true }
    }

    Button(
        onClick = {
            homeViewModel.fetchStoreReload(
                cameraPositionState.contentBounds?.southWest?.latitude,
                cameraPositionState.contentBounds?.southWest?.longitude,
                cameraPositionState.contentBounds?.northEast?.latitude,
                cameraPositionState.contentBounds?.northEast?.longitude
            )
            canReload = false
        },
        modifier = modifier.padding(top = 24.dp),
        enabled = canReload,
        shape = RoundedCornerShape(40.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Royal_Blue,
            contentColor = White,
            disabledBackgroundColor = White,
            disabledContentColor = Black.copy(alpha = .2f)
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = if (canReload) R.drawable.ic_swap_white else R.drawable.ic_swap),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.home_current_location_research),
                fontSize = 12.dp.toSp(),
                fontWeight = FontWeight.Bold,
                fontFamily = pretendard
            )
        }
    }
}

@Composable
private fun CakeStoreContent(
    isReload: Boolean,
    storeList: List<StoreModel>,
    districts: List<DistrictType>,
    storeCount: Int,
    onNavigateToOnBoarding: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    openFilterSheet: () -> Unit,
    onFavoriteClick: (StoreModel) -> Unit,
    onUnFavoriteClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CakkStoreTopBar(
            modifier = Modifier.align(Alignment.Start),
            title = if (isReload) {
                stringResource(id = R.string.home_near_by_cake_shops)
            } else {
                if (districts.isNotEmpty()) {
                    districts.joinToString { it.districtKr }
                } else {
                    stringResource(id = R.string.home_near_by_cake_shops)
                }
            },
            storeCount = storeCount,
            onNavigateToOnBoarding = onNavigateToOnBoarding,
            openFilterSheet = openFilterSheet
        )

        LazyColumn(
            modifier = Modifier.padding(top = 22.dp),
        ) {
            items(storeList) { store ->
                StoreItemContent(
                    modifier = Modifier.fillMaxWidth(),
                    storeModel = store,
                    bookmark = {
                        onFavoriteClick(
                            StoreModel(
                                id = store.id,
                                name = store.name,
                                district = store.district,
                                location = store.location,
                                imageUrls = store.imageUrls,
                                bookmarked = true
                            )
                        )
                    },
                    isFavorite = store.bookmarked,
                    unBookmark = {
                        onUnFavoriteClick(store.id)
                    },
                    onClick = { onNavigateToDetail(store.id) }
                )
            }
        }
    }
}

@Composable
private fun CakkStoreTopBar(
    modifier: Modifier,
    title: String,
    storeCount: Int,
    onNavigateToOnBoarding: () -> Unit,
    openFilterSheet: () -> Unit,
) {
    Image(
        painter = painterResource(id = R.drawable.ic_line),
        contentDescription = null,
        modifier = Modifier.padding(top = 20.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                modifier = modifier.padding(top = 30.dp),
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 18.dp.toSp(),
                color = Raisin_Black,
            )
            Text(
                text = String.format(stringResource(id = R.string.home_num_of_cake_shop), storeCount),
                modifier = modifier.padding(top = 12.dp),
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.dp.toSp(),
                color = Raisin_Black.copy(alpha = 0.8f),
            )
        }
        Row(
            modifier = Modifier.padding(top = 34.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Raisin_Black.copy(alpha = 0.05f)
            ) {
                Text(
                    text = stringResource(id = R.string.home_change_location),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable { onNavigateToOnBoarding() },
                    fontFamily = pretendard,
                    fontSize = 12.dp.toSp(),
                    color = Raisin_Black.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = null,
                modifier = Modifier.clickable { openFilterSheet() }
            )
        }
    }
}

private const val DEFAULT_CLICKED_INEDX = -1

@Composable
@OptIn(ExperimentalNaverMapApi::class)
private fun CakkMap(
    homeViewModel: HomeViewModel,
    cameraPositionState: CameraPositionState,
    bottomSheetType: BottomSheetType,
    fromOnBoarding: Boolean,
    isReload: Boolean,
    storeList: List<StoreModel>,
) {
    val context = LocalContext.current
    var isClickedStoreId by remember {
        mutableStateOf(
            if (bottomSheetType is BottomSheetType.StoreDetail) {
                bottomSheetType.storeId
            } else {
                DEFAULT_CLICKED_INEDX
            }
        )
    }

    if (fromOnBoarding && isReload.not()) {
        if (storeList.isNotEmpty()) {
            when (bottomSheetType) {
                BottomSheetType.StoreList -> {
                    cameraPositionState.position = CameraPosition(LatLng(storeList[0].latitude, storeList[0].longitude), 16.0)
                }

                is BottomSheetType.StoreDetail -> {
                    val detailStore = storeList.find { it.id == bottomSheetType.storeId }
                    detailStore?.let { store ->
                        cameraPositionState.position = CameraPosition(LatLng(store.latitude, store.longitude), 16.0)
                    }
                }

                else -> Unit
            }
        }
    } else {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    cameraPositionState.position = CameraPosition(LatLng(location.latitude, location.longitude), 16.0)
                    homeViewModel.fetchStoreReload(
                        cameraPositionState.contentBounds?.southWest?.latitude,
                        cameraPositionState.contentBounds?.southWest?.longitude,
                        cameraPositionState.contentBounds?.northEast?.latitude,
                        cameraPositionState.contentBounds?.northEast?.longitude
                    )
                }
            }
        }
    }

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
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState,
    ) {
        storeList.forEach { store ->
            Marker(
                state = MarkerState(position = LatLng(store.latitude, store.longitude)),
                icon = OverlayImage.fromResource(
                    if (isClickedStoreId == store.id && bottomSheetType is BottomSheetType.StoreDetail) {
                        R.drawable.ic_clicked_marker
                    } else {
                        R.drawable.ic_marker
                    }
                ),
                isHideCollidedMarkers = true,
                onClick = {
                    isClickedStoreId = store.id
                    homeViewModel.changeBottomSheetType(BottomSheetType.StoreDetail(store.id))
                    homeViewModel.changeBottomSheetState(ExpandedType.HALF)
                    true
                },
            )
        }
    }
}

@Composable
private fun LocationPermission(
    fromOnBoarding: Boolean,
    onNavigateToOnBoarding: () -> Unit
) {
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val settingResultRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            Timber.d("위치 설정이 동의되었습니다.")
            startLocationUpdates()
        } else {
            Timber.d("위치 설정이 거부되었습니다.")
        }
    }

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduceOrNull { acc, next -> acc && next }

        if (areGranted == true) {
            Timber.i("권한이 동의되었습니다.")
        } else {
            Timber.i("권한이 거부되었습니다.")
            onNavigateToOnBoarding()
        }
    }

    LaunchedEffect(true) {
        if (!fromOnBoarding) {
            checkAndRequestPermissions(
                context,
                permissions,
                launcherMultiplePermissions,
            )
            checkAndRequestMyLocationPermission(
                context = context,
                onDisabled = { intentSenderRequest ->
                    settingResultRequest.launch(intentSenderRequest)
                },
                onEnabled = { startLocationUpdates() }
            )
        }
    }
}

fun checkAndRequestPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
) {
    if (permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it,
            ) == PackageManager.PERMISSION_GRANTED
        }
    ) {
        Timber.d("권한이 이미 존재합니다.")
    } else {
        launcher.launch(permissions)
        Timber.d("권한을 요청하였습니다.")
    }
}

@SuppressLint("MissingPermission")
fun checkAndRequestMyLocationPermission(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit,
) {
    val locationRequest = LocationRequest.Builder(1000L)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val gpsSettingTask: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

    gpsSettingTask.addOnSuccessListener {
        onEnabled()
    }

    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest
                    .Builder(exception.resolution)
                    .build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                Timber.i(sendEx.message)
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun startLocationUpdates() {
    locationCallback?.let {
        val locationRequest = LocationRequest.Builder(1000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setDurationMillis(3000L)
            .build()

        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper(),
        )
    }
}
