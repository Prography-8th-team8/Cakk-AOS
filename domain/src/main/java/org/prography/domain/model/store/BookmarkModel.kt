package org.prography.domain.model.store

data class BookmarkModel(
    val id: Int = 0,
    val name: String = "",
    val district: String = "",
    val location: String = "",
    val imageUrls: List<String> = listOf(),
    val bookmarked: Boolean = false,
)
