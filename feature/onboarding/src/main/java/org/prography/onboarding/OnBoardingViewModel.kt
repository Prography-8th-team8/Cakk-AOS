package org.prography.onboarding

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {
    private val _regions = MutableStateFlow(
        listOf(
            RegionItem(0, "도봉 강북 성북 노원", 43, Color(0x1A2448FF)),
            RegionItem(1, "동대문 중랑 성동 광진", 45, Color(0x33FF857D)),
            RegionItem(2, "은평 마포 서대문", 77, Color(0x26FF5CBE)),
            RegionItem(3, "종로 중구 용산", 20, Color(0x33FEDC4D)),
            RegionItem(4, "강서 양천 영등포 구로", 34, Color(0x66FF857D)),
            RegionItem(5, "동작 관악 금천", 28, Color(0x332448FF)),
            RegionItem(6, "서초 강남", 48, Color(0x4DFEDC4D)),
            RegionItem(7, "강동 송파", 48, Color(0x4DFF5CBE))
        )
    )

    val regions: StateFlow<List<RegionItem>> = _regions.asStateFlow()
}
