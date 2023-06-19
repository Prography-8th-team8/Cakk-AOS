package org.prography.domain.mapper

import org.prography.domain.model.district.DistrictGroupModel
import org.prography.domain.model.district.DistrictModel

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
