package org.prography.home.detail

import org.prography.base.BaseState
import org.prography.domain.model.store.StoreDetailModel

data class HomeDetailState(
    val storeDetailModel: StoreDetailModel = StoreDetailModel(),
) : BaseState
