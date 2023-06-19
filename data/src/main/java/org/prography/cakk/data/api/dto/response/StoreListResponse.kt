package org.prography.cakk.data.api.dto.response

import kotlinx.serialization.Serializable
import org.prography.domain.model.store.StoreModel

@Serializable
data class StoreListResponse(
    val id: Int,
    val createdAt: String,
    val modifiedAt: String,
    val name: String,
    val city: String,
    val district: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val storeTypes: List<String>,
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
        storeTypes = storeTypes
    )
}
