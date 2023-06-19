package org.prography.domain.model.store

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
