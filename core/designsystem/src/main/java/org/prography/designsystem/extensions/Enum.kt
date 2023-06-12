package org.prography.designsystem.extensions

import org.prography.cakk.data.api.model.enums.DistrictType
import org.prography.cakk.data.api.model.enums.StoreType
import org.prography.designsystem.ui.theme.*

fun StoreType.toColor() = when (this) {
    StoreType.CHARACTER -> Light_Deep_Pink
    StoreType.LETTERING -> Palatinate_Blue
    StoreType.RICE -> Medium_Slate_Blue
    StoreType.MEALBOX -> Mustard_Yellow
    StoreType.FLOWER -> Metallic_Sunburst
    StoreType.PHOTO -> Congo_Pink
    StoreType.FIGURE -> Yankees_Blue
    StoreType.TIARA -> Cerise
}

fun DistrictType.toColor() = when (this.groupId) {
    1 -> Palatinate_Blue.copy(0.1f)
    2 -> Congo_Pink.copy(0.2f)
    3 -> Light_Deep_Pink.copy(0.15f)
    4 -> Gargoyle_Gas.copy(0.2f)
    5 -> Congo_Pink.copy(0.4f)
    6 -> Palatinate_Blue.copy(0.2f)
    7 -> Gargoyle_Gas.copy(0.3f)
    else -> Light_Deep_Pink.copy(0.3f)
}
