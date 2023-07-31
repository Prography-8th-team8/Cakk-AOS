package org.prography.designsystem.mapper

import org.prography.designsystem.R
import org.prography.domain.model.enums.StoreType

fun StoreType.toIcon() = when (this) {
    StoreType.CHARACTER -> R.drawable.ic_character
    StoreType.LETTERING -> R.drawable.ic_lettering
    StoreType.RICE -> R.drawable.ic_ricecake
    StoreType.MEALBOX -> R.drawable.ic_mealbox
    StoreType.FLOWER -> R.drawable.ic_flower
    StoreType.PHOTO -> R.drawable.ic_photo
    StoreType.FIGURE -> R.drawable.ic_figure
    StoreType.TIARA -> R.drawable.ic_tiara
    StoreType.SOLID -> R.drawable.ic_solid
    StoreType.ETC -> null
}
