package org.prography.cakk.data.api.model.response

import kotlinx.serialization.Serializable

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
)
