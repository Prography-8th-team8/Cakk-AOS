package org.prography.cakk.data.api.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DistrictResponse(
    val district: String,
    val count: Int,
)
