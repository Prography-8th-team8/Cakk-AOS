package org.prography.home.detail

import org.prography.base.BaseState

data class HomeDetailState(
    val storeDetailModel: StoreDetailModel = StoreDetailModel(),
) : BaseState
