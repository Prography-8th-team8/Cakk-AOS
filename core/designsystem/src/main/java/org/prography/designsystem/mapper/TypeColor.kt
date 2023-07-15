package org.prography.designsystem.mapper

import org.prography.domain.model.enums.DistrictType
import org.prography.domain.model.enums.StoreType
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
    StoreType.LUXURY -> Cerise // 추후 색상 변경
    StoreType.SOLID -> Deep_Magenta
    StoreType.ETC -> Raisin_Black
}

fun StoreType.toBackgroundColor(isSelected: Boolean) = when (this) {
    StoreType.CHARACTER -> if (isSelected) Light_Deep_Pink.copy(alpha = 0.2f) else White
    StoreType.LETTERING -> if (isSelected) Palatinate_Blue.copy(alpha = 0.2f) else White
    StoreType.RICE -> if (isSelected) Medium_Slate_Blue.copy(alpha = 0.2f) else White
    StoreType.MEALBOX -> if (isSelected) Mustard_Yellow.copy(alpha = 0.2f) else White
    StoreType.FLOWER -> if (isSelected) Maximum_Red.copy(alpha = 0.12f) else White
    StoreType.PHOTO -> if (isSelected) Yankees_Blue.copy(alpha = 0.2f) else White
    StoreType.FIGURE -> if (isSelected) Metallic_Sunburst.copy(alpha = 0.2f) else White
    StoreType.TIARA -> if (isSelected) Palatinate_Blue.copy(alpha = 0.1f) else White
    StoreType.LUXURY -> White // 추후 색상 변경
    StoreType.SOLID -> if (isSelected) Cerise.copy(alpha = 0.2f) else White
    StoreType.ETC -> if (isSelected) Raisin_Black.copy(alpha = 0.2f) else White
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
