package org.prography.base

import androidx.annotation.DrawableRes

interface BaseBottomDestination {
    val label: String

    @get:DrawableRes
    val icon: Int
}