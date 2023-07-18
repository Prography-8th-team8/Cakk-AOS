package org.prography.network.api.dto.response

import kotlinx.serialization.Serializable
import org.prography.domain.model.store.StoreDetailModel

@Serializable
data class StoreDetailResponse(
    val address: String,
    val city: String,
    val createdAt: String,
    val description: String,
    val district: String,
    val id: Int,
    val latitude: Double,
    val link: String,
    val location: String,
    val longitude: Double,
    val modifiedAt: String,
    val name: String,
    val phoneNumber: String,
    val imageUrls: List<String>,
    val storeTypes: List<String>,
) {
    fun toModel() = StoreDetailModel(
        address = address,
        city = city,
        createdAt = createdAt,
        description = description,
        district = district,
        id = id,
        latitude = latitude,
        link = link,
        location = location,
        longitude = longitude,
        modifiedAt = modifiedAt,
        name = name,
        phoneNumber = phoneNumber,
        imageUrls = imageUrls,
        storeTypes = storeTypes
    )
}
