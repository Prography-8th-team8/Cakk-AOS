package org.prography.domain.model.store

data class FeedModel(
    val storeId: Int,
    val storeName: String,
    val district: String,
    val imageUrl: String
)
