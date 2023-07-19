package org.prography.network.api.dto.response

import kotlinx.serialization.Serializable
import org.prography.domain.model.store.StoreModel

@Serializable
data class StoreReloadResponse(
    val id: Int,
    val createdAt: String,
    val modifiedAt: String,
    val name: String,
    val shareLink: String,
    val city: String,
    val district: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val thumbnail: String?,
    val imageUrls: List<String>,
    val storeTypes: List<String>
) {
    fun toModel() = StoreModel(
        id = id,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
        name = name,
        city = city,
        district = district,
        location = location,
        latitude = latitude,
        longitude = longitude,
        imageUrls = imageUrls,
        storeTypes = storeTypes
    )
}
