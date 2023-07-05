package org.prography.network.api.dto.response

import kotlinx.serialization.Serializable
import org.prography.domain.model.store.BlogPostModel

@Serializable
data class BlogPostResponse(
    val title: String,
    val link: String,
    val description: String,
    val bloggername: String,
    val bloggerlink: String,
    val postdate: String
) {
    fun toModel() = BlogPostModel(
        title = title,
        link = link,
        description = description,
        bloggername = bloggername,
        bloggerlink = bloggerlink,
        postdate = postdate
    )
}
