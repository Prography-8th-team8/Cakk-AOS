package org.prography.domain.model.store

data class BlogPostModel(
    val title: String,
    val link: String,
    val description: String,
    val bloggername: String,
    val bloggerlink: String,
    val postdate: String
)
