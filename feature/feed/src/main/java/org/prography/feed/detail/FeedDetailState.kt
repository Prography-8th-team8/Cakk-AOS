package org.prography.feed.detail

import org.prography.base.BaseState
import org.prography.domain.model.store.StoreDetailModel

data class FeedDetailState(
    val storeDetailModel: StoreDetailModel = StoreDetailModel()
) : BaseState
