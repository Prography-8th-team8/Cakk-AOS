package org.prography.cakk.data.api.model.request

import kotlinx.serialization.Serializable

@Serializable
data class StoreListRequest(
    val district: String,
    val page: Int,
)
