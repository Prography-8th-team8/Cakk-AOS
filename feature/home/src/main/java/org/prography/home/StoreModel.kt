package org.prography.home

import org.prography.cakk.data.api.model.response.StoreListResponse

data class StoreModel(
    val id: Int = 0,
    val createdAt: String = "",
    val modifiedAt: String = "",
    val name: String = "",
    val city: String = "",
    val district: String = "",
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val storeTypes: List<String> = listOf(),
)

fun StoreListResponse.toModel() = StoreModel(
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

fun List<StoreListResponse>.toModel() = map { it.toModel() }
