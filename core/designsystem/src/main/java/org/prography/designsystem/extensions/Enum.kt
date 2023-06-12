package org.prography.designsystem.extensions

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
