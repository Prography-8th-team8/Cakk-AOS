package org.prography.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.overlay.OverlayImage
import org.prography.designsystem.R

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val storeList = homeViewModel.stores.collectAsState().value

    LaunchedEffect(true) {
        homeViewModel.fetchStoreList()
    }

    Box {
        NaverMap(
            modifier = Modifier.fillMaxSize()
        ) {
            storeList?.forEach {
                Marker(
                    // 나중에 it.latitude, it.longitude로 변경
                    state = MarkerState(position = LatLng(37.532600, 127.024612)),
                    icon = OverlayImage.fromResource(R.drawable.ic_marker),
                )
            }
        }
    }
}
