package org.prography.network.api.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class FeedResponse(
    val storeId: Int,
    val storeName: String,
    val district: String,
    val imageUrl: String
)
