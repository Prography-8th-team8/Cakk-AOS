package org.prography.home.detail

import org.prography.base.BaseState
import org.prography.domain.model.store.BlogPostModel
import org.prography.domain.model.store.StoreDetailModel

const val DEFAULT_BLOG_POST_COUNT = 3
data class HomeDetailState(
    val storeDetailModel: StoreDetailModel = StoreDetailModel(),
    val blogPosts: List<BlogPostModel> = listOf(),
    val showBlogPostCount: Int = DEFAULT_BLOG_POST_COUNT
) : BaseState
