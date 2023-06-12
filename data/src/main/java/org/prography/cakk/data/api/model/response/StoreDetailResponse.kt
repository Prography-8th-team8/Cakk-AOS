package org.prography.cakk.data.api.model.response

import kotlinx.serialization.Serializable

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
    val storeTypes: List<String>,
)
