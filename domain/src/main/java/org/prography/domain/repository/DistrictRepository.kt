package org.prography.domain.repository

import kotlinx.coroutines.flow.Flow
import org.prography.domain.model.district.DistrictModel

interface DistrictRepository {
    fun fetchDistrictList(): Flow<List<DistrictModel>>
}
