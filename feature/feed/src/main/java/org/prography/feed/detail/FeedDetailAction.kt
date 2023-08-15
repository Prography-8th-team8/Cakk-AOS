package org.prography.feed.detail

import org.prography.base.BaseAction
import org.prography.domain.model.store.StoreDetailModel

sealed class FeedDetailAction : BaseAction {
    object Loading : FeedDetailAction()
    data class LoadDetailInfo(val storeDetailModel: StoreDetailModel) : FeedDetailAction()
    data class LoadBookmarkedCakeShop(val id: Int?) : FeedDetailAction()
    data class BookmarkCakeShop(val id: Int?) : FeedDetailAction()
    data class UnBookmarkCakeShop(val id: Int?) : FeedDetailAction()
}
