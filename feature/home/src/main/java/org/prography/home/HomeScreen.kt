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
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import org.prography.domain.model.enums.DistrictType
import org.prography.domain.model.enums.StoreType
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.prography.designsystem.R
import org.prography.designsystem.mapper.toBackgroundColor
import org.prography.designsystem.mapper.toColor
import org.prography.designsystem.mapper.toIcon
import org.prography.designsystem.ui.theme.*
import org.prography.domain.model.store.StoreModel
import org.prography.utility.extensions.toSp
import org.prography.utility.navigation.destination.CakkDestination.Home.DEFAULT_DISTRICTS_INFO
import org.prography.utility.navigation.destination.CakkDestination.Home.DEFAULT_STORE_COUNT
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

    LocationPermission(
        fromOnBoarding = fromOnBoarding,
        permissions = permissions,
        settingResultRequest = settingResultRequest,
        launcherMultiplePermissions = launcherMultiplePermissions
    )

    val homeState = homeViewModel.state.collectAsStateWithLifecycle()

    BottomSheet(
        homeViewModel = homeViewModel,
        fromOnBoarding = fromOnBoarding,
        isReload = homeState.value.isReload,
        storeList = homeState.value.storeModels,
        districts = if (districtsArg.isNotEmpty()) districtsArg.split(" ").map { DistrictType.getName(it) } else listOf(),
        storeCount = if (storeCountArg >= 0 && homeState.value.isReload.not()) storeCountArg else homeState.value.storeModels.size,
        bottomExpandedType = homeState.value.lastExpandedType,
        districtsArg = districtsArg,
        onNavigateToOnBoarding = onNavigateToOnBoarding,
        onNavigateToDetail = onNavigateToDetail
    )

    LaunchedEffect(homeViewModel) {
        if (homeState.value.storeModels.isEmpty()) {
            if (districtsArg.isEmpty()) {
                homeViewModel.sendAction(HomeUiAction.LoadStoreList(listOf("JONGNO"), StoreType.values().joinToString(",") { it.name }))
            } else {
                homeViewModel.sendAction(
                    HomeUiAction.LoadStoreList(
                        districtsArg.split(" "),
                        StoreType.values().joinToString(",") { it.name }
                    )
                )
            }
        }
    }
}

@SuppressLint("InternalInsetResource", "DiscouragedApi")
@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
private fun BottomSheet(
    homeViewModel: HomeViewModel = hiltViewModel(),
    fromOnBoarding: Boolean,
    isReload: Boolean,
    storeList: List<StoreModel>,
    districts: List<DistrictType>,
    storeCount: Int,
    bottomExpandedType: ExpandedType,
    districtsArg: String,
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
    var offsetY by rememberSaveable { mutableStateOf(((screenHeight / 2.5).toInt()).dp.value) }
    var expandedType by rememberSaveable { mutableStateOf(bottomExpandedType) }
    val height by animateDpAsState(expandedType.getByScreenHeight(expandedType, screenHeight, statusBarHeight, offsetY))

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
                    .height(height)
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
                                }
                            },
                            onDragEnd = {
                                expandedType = when {
                                    offsetY >= (screenHeight / 1.5).toFloat() -> {
                                        homeViewModel.sendAction(HomeUiAction.BottomSheetExpandFull)
                                        ExpandedType.FULL
                                    }
                                    offsetY >= (screenHeight / 4).toFloat() && offsetY < (screenHeight / 1.5).toFloat() -> {
                                        homeViewModel.sendAction(HomeUiAction.BottomSheetExpandQuarter)
                                        ExpandedType.QUARTER
                                    }
                                    else -> {
                                        homeViewModel.sendAction(HomeUiAction.BottomSheetExpandCollapsed)
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
                if (expandedType != ExpandedType.HALF) {
                    BottomSheetContent(
                        isReload = isReload,
                        storeList = storeList,
                        districts = districts,
                        storeCount = storeCount,
                        onNavigateToOnBoarding = onNavigateToOnBoarding,
                        onNavigateToDetail = onNavigateToDetail,
                        openFilterSheet = {
                            homeViewModel.sendAction(HomeUiAction.BottomSheetExpandHalf)
                            expandedType = ExpandedType.HALF
                            offsetY = expandedType
                                .getByScreenHeight(expandedType, screenHeight, statusBarHeight, offsetY)
                                .value
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 20.dp),
                    ) {
                        val selectFilter = remember {
                            mutableStateListOf(false, false, false, false, false, false, false, false, false, false, false)
                        }
                        val filters = remember { mutableStateOf("") }

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painterResource(R.drawable.ic_close),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 22.dp)
                                    .align(Alignment.End)
                                    .clickable {
                                        homeViewModel.sendAction(HomeUiAction.BottomSheetExpandQuarter)
                                        expandedType = ExpandedType.QUARTER
                                        offsetY = expandedType
                                            .getByScreenHeight(expandedType, screenHeight, statusBarHeight, offsetY)
                                            .value
                                    },
                            )
                            Text(
                                text = stringResource(id = R.string.home_filter),
                                fontFamily = pretendard,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.dp.toSp(),
                                color = Black,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Row(
                                modifier = Modifier.padding(top = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.home_filter_recommend),
                                    fontFamily = pretendard,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.dp.toSp(),
                                    color = Black.copy(alpha = 0.6f),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Image(
                                    painterResource(id = R.drawable.ic_refresh),
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        selectFilter.indices.forEach { selectFilter[it] = false }
                                    }
                                )
                            }

                            FlowRow(
                                modifier = Modifier.padding(top = 15.dp),
                                content = {
                                    StoreType.values().forEachIndexed { index, storeType ->
                                        Surface(
                                            shape = RoundedCornerShape(50.dp),
                                            color = storeType.toBackgroundColor(isSelected = selectFilter[index]),
                                            border = BorderStroke(1.dp, Raisin_Black.copy(alpha = 0.1f)),
                                            modifier = Modifier
                                                .padding(end = 4.dp, bottom = 6.dp)
                                                .toggleable(
                                                    value = selectFilter[index],
                                                    onValueChange = {
                                                        selectFilter[index] = !selectFilter[index]
                                                    }
                                                )
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                storeType.toIcon()?.let { icon ->
                                                    Image(painter = painterResource(id = icon), contentDescription = null)
                                                }
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = storeType.tag,
                                                    fontFamily = pretendard,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 14.dp.toSp(),
                                                    color = Raisin_Black
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(15.dp),
                            color = if (selectFilter.count { it } > 0) Light_Deep_Pink else Raisin_Black.copy(alpha = 0.2f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 4.dp)
                                .clickable(enabled = selectFilter.count { it } > 0) {
                                    StoreType.values()
                                        .forEachIndexed { index, storeType ->
                                            if (selectFilter[index]) filters.value += "${storeType.name},"
                                        }
                                    homeViewModel.sendAction(
                                        HomeUiAction.LoadStoreList(
                                            districtsArg.split(" "),
                                            filters.value
                                        )
                                    )
                                    homeViewModel.sendAction(HomeUiAction.BottomSheetExpandQuarter)
                                    expandedType = ExpandedType.QUARTER
                                    offsetY = expandedType
                                        .getByScreenHeight(expandedType, screenHeight, statusBarHeight, offsetY)
                                        .value
                                },
                        ) {
                            Text(
                                text = stringResource(id = R.string.home_apply),
                                fontFamily = pretendard,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp),
                                fontSize = 18.dp.toSp(),
                                color = White
                            )
                        }
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
                cameraPositionState = cameraPositionState,
                fromOnBoarding = fromOnBoarding,
                isReload = isReload,
                storeList = storeList
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
        modifier = modifier.padding(top = 24.dp),
        onClick = {
            homeViewModel.fetchStoreReload(
                cameraPositionState.contentBounds?.southWest?.latitude,
                cameraPositionState.contentBounds?.southWest?.longitude,
                cameraPositionState.contentBounds?.northEast?.latitude,
                cameraPositionState.contentBounds?.northEast?.longitude
            )
            canReload = false
        },
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
private fun BottomSheetContent(
    isReload: Boolean,
    storeList: List<StoreModel>,
    districts: List<DistrictType>,
    storeCount: Int,
    onNavigateToOnBoarding: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    openFilterSheet: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BottomSheetTop(
            modifier = Modifier.align(Alignment.Start),
            title = if (isReload) {
                stringResource(id = R.string.home_current_map_location)
            } else {
                if (districts.isNotEmpty()) {
                    districts.joinToString { it.districtKr }
                } else {
                    stringResource(id = R.string.home_current_location)
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
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clickable { onNavigateToDetail(store.id) },
                    shape = RoundedCornerShape(24.dp),
                    color = Old_Lace
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
private fun StoreInfo(store: StoreModel) {
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
private fun StoreTags(store: StoreModel) {
    Row(modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)) {
        store.storeTypes.forEach { storeType ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = StoreType.valueOf(storeType).toColor().copy(alpha = 0.2f)
            ) {
                Text(
                    text = StoreType.valueOf(storeType).tag,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                    color = StoreType.valueOf(storeType).toColor(),
                    fontSize = 12.dp.toSp(),
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold
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
                    fontSize = 12.dp.toSp(),
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BottomSheetTop(
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
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 18.dp.toSp(),
                color = Raisin_Black,
                modifier = modifier
                    .padding(top = 30.dp)
            )
            Text(
                text = "$storeCount" + stringResource(id = R.string.home_num_of_cake_shop),
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.dp.toSp(),
                color = Raisin_Black.copy(alpha = 0.8f),
                modifier = modifier
                    .padding(top = 12.dp)
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

@Composable
@OptIn(ExperimentalNaverMapApi::class)
private fun CakkMap(
    cameraPositionState: CameraPositionState,
    fromOnBoarding: Boolean,
    isReload: Boolean,
    storeList: List<StoreModel>,
) {
    val context = LocalContext.current
    if (fromOnBoarding && isReload.not()) {
        if (storeList.isNotEmpty()) {
            cameraPositionState.position = CameraPosition(LatLng(storeList[0].latitude, storeList[0].longitude), 16.0)
        }
    } else {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    cameraPositionState.position = CameraPosition(LatLng(location.latitude, location.longitude), 16.0)
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
                icon = OverlayImage.fromResource(R.drawable.ic_marker),
            )
        }
    }
}

@Composable
private fun LocationPermission(
    fromOnBoarding: Boolean,
    permissions: Array<String>,
    settingResultRequest: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    launcherMultiplePermissions: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
) {
    val context = LocalContext.current

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
