package org.prography.home.detail

import org.prography.cakk.data.api.model.response.StoreDetailResponse

data class StoreDetailModel(
    val address: String = "",
    val city: String = "",
    val createdAt: String = "",
    val description: String = "",
    val district: String = "",
    val id: Int = 0,
    val latitude: Double = 0.0,
    val link: String = "",
    val location: String = "",
    val longitude: Double = 0.0,
    val modifiedAt: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val storeTypes: List<String> = listOf(),
)

fun StoreDetailResponse.toModel() = StoreDetailModel(
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
    storeTypes = storeTypes
)
