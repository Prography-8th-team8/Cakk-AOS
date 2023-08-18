package org.prography.home

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.prography.base.BaseState
import org.prography.domain.model.enums.StoreType
import org.prography.domain.model.store.StoreModel

data class HomeUiState(
    val storeModels: List<StoreModel> = listOf(),
    val storeTypes: String = StoreType.values().joinToString(","),
    val bottomSheetType: BottomSheetType = BottomSheetType.StoreList,
    val expandedType: ExpandedType = ExpandedType.QUARTER,
    val isReload: Boolean = false
) : BaseState

sealed class BottomSheetType {
    object StoreList : BottomSheetType()

    class StoreDetail(val storeId: Int) : BottomSheetType()

    object Filter : BottomSheetType()
}

enum class ExpandedType {
    HALF, FULL, COLLAPSED, QUARTER, MOVING;

    fun getByScreenHeight(type: ExpandedType, screenHeight: Int, statusBarHeight: Int, offsetY: Float): Dp {
        return when (type) {
            FULL -> {
                (screenHeight - statusBarHeight).dp
            }

            QUARTER -> {
                ((screenHeight / 2.5).toInt()).dp
            }

            COLLAPSED -> {
                53.dp
            }

            HALF -> {
                (screenHeight / 2).dp
            }

            MOVING -> {
                offsetY.dp
            }
        }
    }
}
