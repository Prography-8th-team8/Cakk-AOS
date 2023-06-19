package org.prography.network.api.dto.response

import kotlinx.serialization.Serializable
import org.prography.cakk.data.api.model.enums.DistrictType
import org.prography.domain.model.district.DistrictModel

@Serializable
data class DistrictResponse(
    val district: String,
    val count: Int,
) {
    fun toModel() = DistrictModel(
        district = DistrictType.valueOf(district),
        count = count
    )
}
