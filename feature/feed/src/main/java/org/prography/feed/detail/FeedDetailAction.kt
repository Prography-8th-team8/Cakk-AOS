package org.prography.feed.detail

import org.prography.base.BaseAction
import org.prography.domain.model.store.StoreDetailModel

sealed class FeedDetailAction : BaseAction {
    object Loading : FeedDetailAction()

    data class LoadDetailInfo(
        val storeDetailModel: StoreDetailModel
    ) : FeedDetailAction()
}
