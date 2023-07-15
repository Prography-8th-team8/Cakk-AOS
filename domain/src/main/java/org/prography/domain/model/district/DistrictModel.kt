package org.prography.domain.model.district

import org.prography.domain.model.enums.DistrictType

data class DistrictModel(
    val district: DistrictType,
    val count: Int,
)
