package org.prography.network.api.dto.response

import kotlinx.serialization.Serializable
import org.prography.domain.model.store.FeedModel

@Serializable
data class FeedResponse(
    val storeId: Int,
    val storeName: String,
    val district: String,
    val imageUrl: String
) {
    fun toModel() = FeedModel(
        storeId = storeId,
        storeName = storeName,
        district = district,
        imageUrl = imageUrl
    )
}
