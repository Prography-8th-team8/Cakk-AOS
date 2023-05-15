package org.prography.home

data class StoreModel(
    val id: Long = 0L,
    val createdAt: String = "",
    val modifiedAt: String = "",
    val name: String = "",
    val city: String = "",
    val district: String = "",
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val storeType: List<StoreType> = listOf()
)
