package org.prography.home.detail

import org.prography.base.BaseState
import org.prography.domain.model.store.BlogPostModel
import org.prography.domain.model.store.StoreDetailModel

data class HomeDetailState(
    val storeDetailModel: StoreDetailModel = StoreDetailModel(),
    val blogPosts: List<BlogPostModel> = listOf()
) : BaseState
