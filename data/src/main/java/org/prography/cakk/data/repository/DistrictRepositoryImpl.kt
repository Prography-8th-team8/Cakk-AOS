package org.prography.cakk.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.prography.cakk.data.datasource.DistrictRemoteSource
import org.prography.domain.model.district.DistrictModel
import org.prography.domain.repository.DistrictRepository
import org.prography.utility.mapper.toModel
import javax.inject.Inject

class DistrictRepositoryImpl @Inject constructor(
    private val districtRemoteSource: DistrictRemoteSource
) : DistrictRepository {
    override fun fetchDistrictList(): Flow<List<DistrictModel>> =
        districtRemoteSource.fetchDistrictList().map { it.toModel() }
}
