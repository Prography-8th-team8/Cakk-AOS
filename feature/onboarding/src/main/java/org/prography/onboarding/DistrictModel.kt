package org.prography.onboarding

import org.prography.cakk.data.api.model.enums.DistrictType
import org.prography.cakk.data.api.model.response.DistrictResponse

data class DistrictModel(
    val district: DistrictType,
    val count: Int
)

fun DistrictResponse.toModel() = DistrictModel(
    district = DistrictType.valueOf(district),
    count = count
)
