package org.prography.cakk.data.repository.district

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.prography.cakk.data.api.model.response.DistrictResponse
import org.prography.network.CakkService.BASE_URL
import org.prography.network.CakkService.Endpoint.DISTRICT_LIST
import javax.inject.Inject

class DistrictRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : DistrictRepository {
    override fun fetchDistrictList(): Flow<DistrictResponse> = flow {
        emit(
            httpClient.get {
                url("$BASE_URL$DISTRICT_LIST")
            }
        )
    }
}
