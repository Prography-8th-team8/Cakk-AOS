package org.prography.network.api.dto.response

import kotlinx.serialization.Serializable
import org.prography.domain.model.store.StoreBlogModel

@Serializable
data class StoreBlogResponse(
    val blogPosts: List<BlogPostResponse>
) {
    fun toModel() = StoreBlogModel(
        blogPosts = blogPosts.map { it.toModel() }
    )
}
