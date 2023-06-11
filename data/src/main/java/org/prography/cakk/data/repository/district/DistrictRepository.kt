package org.prography.cakk.data.repository.district

import kotlinx.coroutines.flow.Flow
import org.prography.cakk.data.api.model.response.DistrictResponse

interface DistrictRepository {
    fun fetchDistrictList(): Flow<DistrictResponse>
}
