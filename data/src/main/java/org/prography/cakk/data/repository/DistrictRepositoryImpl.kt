package org.prography.cakk.data.repository

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.prography.cakk.data.api.model.response.DistrictResponse
import org.prography.domain.model.district.DistrictModel
import org.prography.domain.repository.DistrictRepository
import org.prography.network.CakkService.BASE_URL
import org.prography.network.CakkService.Endpoint.DISTRICT_LIST
import javax.inject.Inject

class DistrictRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : DistrictRepository {
    override fun fetchDistrictList(): Flow<List<DistrictModel>> = flow {
        emit(
            httpClient.get<List<DistrictResponse>> {
                url("$BASE_URL$DISTRICT_LIST")
            }.map { it.toModel() }
        )
    }
}
