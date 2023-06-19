package org.prography.domain.model.district

data class DistrictGroupModel(
    val districts: List<DistrictModel> = listOf(),
    val count: Int,
)
