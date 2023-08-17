package org.prography.home

import org.prography.base.BaseSideEffect

sealed class HomeSideEffect : BaseSideEffect {
    object ReloadError : HomeSideEffect()

    object FilterCakeShop : HomeSideEffect()
}
