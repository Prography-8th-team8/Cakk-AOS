package org.prography.network.api.dto.response

import org.prography.domain.model.store.StoreBlogModel

@kotlinx.serialization.Serializable
data class StoreBlogResponse(
    val blogPosts: List<BlogPostResponse>
) {
    fun toModel() = StoreBlogModel(
        blogPosts = blogPosts.map { it.toModel() }
    )
}
