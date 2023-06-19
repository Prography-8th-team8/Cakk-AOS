package org.prography.domain.model.district

import org.prography.cakk.data.api.model.enums.DistrictType

data class DistrictModel(
    val district: DistrictType,
    val count: Int,
)

// fun DistrictResponse.toModel() = DistrictModel(
//    district = DistrictType.valueOf(district),
//    count = count
// )
