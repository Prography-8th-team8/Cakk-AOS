package org.prography.home.detail

import org.prography.base.BaseAction
import org.prography.domain.model.store.BlogPostModel
import org.prography.domain.model.store.StoreDetailModel

sealed class HomeDetailAction : BaseAction {
    object Loading : HomeDetailAction()
    data class LoadDetailInfo(val storeDetailModel: StoreDetailModel) : HomeDetailAction()
    data class LoadBlogInfos(val blogPosts: List<BlogPostModel>) : HomeDetailAction()
    data class ChangeShowBlogCount(val count: Int) : HomeDetailAction()
}
