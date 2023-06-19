package org.prography.utility.mapper

import org.prography.domain.model.district.DistrictGroupModel
import org.prography.domain.model.district.DistrictModel
import org.prography.network.api.dto.response.DistrictResponse

fun List<DistrictModel>.toGroup(): List<DistrictGroupModel> =
    this.sortedBy { it.district.groupId }
        .groupBy { it.district.groupId }
        .values
        .map {
            DistrictGroupModel(
                districts = it,
                count = it.fold(0) { acc, districtModel -> acc + districtModel.count }
            )
        }

fun List<DistrictResponse>.toModel(): List<DistrictModel> = map { it.toModel() }
