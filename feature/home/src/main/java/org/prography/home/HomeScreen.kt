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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.location.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.tasks.Task
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import org.prography.cakk.data.api.model.response.StoreListResponse
import org.prography.designsystem.R
import org.prography.designsystem.ui.theme.*
import org.prography.cakk.data.api.model.enums.DistrictType
import org.prography.cakk.data.api.model.enums.StoreType
import org.prography.designsystem.extensions.toColor
import org.prography.utility.extensions.toSp
import org.prography.utility.navigation.destination.CakkDestination
import timber.log.Timber

enum class ExpandedType {
    HALF, FULL, COLLAPSED, MOVING;

    fun getByScreenHeight(type: ExpandedType, screenHeight: Int, statusBarHeight: Int, offsetY: Float): Dp {
        return when (type) {
            FULL -> {
                (screenHeight - statusBarHeight).dp
            }
            HALF -> {
                ((screenHeight / 2.5).toInt()).dp
            }
            COLLAPSED -> {
                53.dp
            }
            MOVING -> {
                offsetY.dp
            }
        }
    }
}

private var locationCallback: LocationCallback? = null
var fusedLocationClient: FusedLocationProviderClient? = null

@SuppressLint("InternalInsetResource", "DiscouragedApi")
@Composable
fun HomeScreen(
    navHostController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = hiltViewModel(),
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
            navHostController.navigate(CakkDestination.OnBoarding.route) {
                popUpTo(CakkDestination.Home.route) {
                    inclusive = true
                }
            }
        }
    }

    LocationPermission(navHostController, context, permissions, settingResultRequest, launcherMultiplePermissions)

    val storeList by homeViewModel.stores.collectAsStateWithLifecycle()
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val statusBarHeight = LocalContext.current.resources.getDimensionPixelSize(
        LocalContext.current.resources.getIdentifier(
            stringResource(id = R.string.home_status_bar_height),
            stringResource(id = R.string.home_dimen),
            stringResource(id = R.string.home_android),
        ),
    )
    BottomSheet(
        navHostController,
        settingResultRequest,
        context,
        storeList = storeList,
        screenHeight = screenHeight,
        statusBarHeight = statusBarHeight,
        navigateToOnBoarding = {
            navHostController.navigate(CakkDestination.OnBoarding.route) {
                popUpTo(CakkDestination.Home.route) {
                    inclusive = true
                }
            }
        }
    ) { storeId ->
        navHostController.navigate("${CakkDestination.HomeDetail.route}/$storeId")
    }
}

@SuppressLint("InternalInsetResource", "DiscouragedApi")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheet(
    navHostController: NavHostController,
    settingResultRequest: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    context: Context,
    storeList: List<StoreListResponse>,
    screenHeight: Int,
    statusBarHeight: Int,
    navigateToOnBoarding: () -> Unit,
    navigateToDetail: (Int) -> Unit,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Expanded),
    )
    var offsetY by remember { mutableStateOf(((screenHeight / 2.5).toInt()).dp.value) }
    var expandedType by remember { mutableStateOf(ExpandedType.HALF) }
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
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                expandedType = ExpandedType.MOVING
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
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
                                        ExpandedType.COLLAPSED
                                    }
                                }
                                offsetY = expandedType
                                    .getByScreenHeight(expandedType, screenHeight, statusBarHeight, offsetY)
                                    .value
                            },
                        )
                    }
                    .background(White),
            ) {
                BottomSheetContent(storeList, navigateToOnBoarding, navigateToDetail)
            }
        },
        sheetPeekHeight = height,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CakkMap(storeList, navHostController, context)
            SearchArea(modifier = Modifier.align(Alignment.TopCenter), context, settingResultRequest)
        }
    }
}

@Composable
private fun SearchArea(
    modifier: Modifier,
    context: Context,
    settingResultRequest: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
) {
    Button(
        modifier = modifier.padding(top = 24.dp),
        onClick = {
            checkAndRequestMyLocationPermission(
                context = context,
                onDisabled = { intentSenderRequest ->
                    settingResultRequest.launch(intentSenderRequest)
                },
                onEnabled = { startLocationUpdates() }
            )
        },
        shape = RoundedCornerShape(40.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = White, contentColor = Black.copy(alpha = .2f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.ic_swap), contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "이 지역 재검색",
                fontSize = 12.dp.toSp(),
                fontWeight = FontWeight.Bold,
                fontFamily = pretendard
            )
        }
    }
}

@Composable
private fun BottomSheetContent(
    storeList: List<StoreListResponse>,
    navigateToOnBoarding: () -> Unit,
    navigateToDetail: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BottomSheetTop(modifier = Modifier.align(Alignment.Start), navigateToOnBoarding)

        LazyColumn(
            modifier = Modifier.padding(top = 22.dp),
        ) {
            items(storeList) { store ->
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clickable { navigateToDetail(store.id) },
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
private fun BottomSheetTop(modifier: Modifier, navigateToOnBoarding: () -> Unit) {
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
                text = "은평, 마포, 서대문",
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 18.dp.toSp(),
                color = Raisin_Black,
                modifier = modifier
                    .padding(top = 30.dp)
            )
            Text(
                text = "24개의 케이크샵",
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.dp.toSp(),
                color = Raisin_Black.copy(alpha = 0.8f),
                modifier = modifier
                    .padding(top = 12.dp)
            )
        }
        Surface(
            modifier = Modifier.padding(top = 34.dp),
            shape = RoundedCornerShape(12.dp),
            color = Light_Deep_Pink.copy(alpha = 0.15f)
        ) {
            Text(
                text = stringResource(id = R.string.home_change_location),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable {
                        navigateToOnBoarding()
                    },
                fontFamily = pretendard,
                fontSize = 12.dp.toSp(),
                color = Magenta,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
@OptIn(ExperimentalNaverMapApi::class)
private fun CakkMap(storeList: List<StoreListResponse>, navHostController: NavHostController, context: Context) {
    val fromOnBoarding = navHostController.previousBackStackEntry?.destination?.route == CakkDestination.OnBoarding.route

    val cameraPositionState: CameraPositionState = rememberCameraPositionState { position = CameraPosition(LatLng(0.0, 0.0), 16.0) }

    if (fromOnBoarding) {
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
    navHostController: NavHostController,
    context: Context,
    permissions: Array<String>,
    settingResultRequest: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    launcherMultiplePermissions: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
) {
    LaunchedEffect(true) {
        val fromOnBoarding = navHostController.previousBackStackEntry?.destination?.route == CakkDestination.OnBoarding.route

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
