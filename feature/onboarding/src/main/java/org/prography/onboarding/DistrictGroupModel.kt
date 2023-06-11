package org.prography.onboarding

data class DistrictGroupModel(
    val districts: List<DistrictModel> = listOf(),
    val count: Int,
)

fun List<DistrictModel>.toGroup() =
    this.sortedBy { it.district.groupId }
        .groupBy { it.district.groupId }
        .values
        .map {
            DistrictGroupModel(
                districts = it,
                count = it.fold(0) { acc, districtModel -> acc + districtModel.count }
            )
        }
