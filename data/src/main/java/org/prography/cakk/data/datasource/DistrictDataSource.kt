package org.prography.cakk.data.datasource

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.prography.network.CakkService
import org.prography.network.api.dto.response.DistrictResponse
import javax.inject.Inject

class DistrictDataSource @Inject constructor(
    private val httpClient: HttpClient
) {
    fun fetchDistrictList(): Flow<List<DistrictResponse>> = flow {
        emit(
            httpClient.get {
                url("${CakkService.BASE_URL}${CakkService.Endpoint.DISTRICT_LIST}")
            }
        )
    }
}
