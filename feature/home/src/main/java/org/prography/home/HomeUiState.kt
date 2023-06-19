package org.prography.home

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.prography.base.BaseState
import org.prography.domain.model.store.StoreModel

data class HomeUiState(
    val storeModels: List<StoreModel> = listOf(),
    val lastExpandedType: ExpandedType = ExpandedType.HALF,
) : BaseState

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
