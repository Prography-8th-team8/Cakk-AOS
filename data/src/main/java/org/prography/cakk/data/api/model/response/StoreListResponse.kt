package org.prography.cakk.data.api.model.response

import kotlinx.serialization.Serializable

@Serializable
data class StoreListResponse(
    val city: String,
    val createdAt: String,
    val district: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val modifiedAt: String,
    val name: String,
    val storeTypes: List<String>,
)
