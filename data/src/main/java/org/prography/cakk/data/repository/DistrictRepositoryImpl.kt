package org.prography.cakk.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.prography.cakk.data.datasource.DistrictDataSource
import org.prography.domain.model.district.DistrictModel
import org.prography.domain.repository.DistrictRepository
import org.prography.utility.mapper.toModel
import javax.inject.Inject

class DistrictRepositoryImpl @Inject constructor(
    private val districtDataSource: DistrictDataSource
) : DistrictRepository {
    override fun fetchDistrictList(): Flow<List<DistrictModel>> =
        districtDataSource.fetchDistrictList().map { it.toModel() }
}
