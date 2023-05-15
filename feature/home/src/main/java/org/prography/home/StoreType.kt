package org.prography.home

import androidx.compose.ui.graphics.Color
import org.prography.designsystem.ui.theme.*

enum class StoreType(val tag: String, val color: Color) {
    CHARACTER("캐릭터", Light_Deep_Pink),
    LETTERING("레터링", Palatinate_Blue),
    RICE("떡케이크", Medium_Slate_Blue),
    MEALBOX("도시락", Mustard_Yellow),
    FLOWER("플라워", Metallic_Sunburst),
    PHOTO("포토", Congo_Pink),
    FIGURE("피규어", Yankees_Blue),
    TIARA("티아라", Cerise),
}
