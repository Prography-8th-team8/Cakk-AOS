package org.prography.domain.model.store

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
    val thumbnail: String? = null,
    val modifiedAt: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val imageUrls: List<String> = listOf(),
    val storeTypes: List<String> = listOf(),
    val bookmarked: Boolean = false,
)
