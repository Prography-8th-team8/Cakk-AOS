package org.prography.cakk.data.datasource

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.prography.network.CakkService
import org.prography.network.api.dto.response.StoreDetailResponse
import org.prography.network.api.dto.response.StoreResponse
import javax.inject.Inject

class StoreRemoteSource @Inject constructor(
    private val httpClient: HttpClient
) {
    fun fetchStoreList(
        district: String,
        page: Int
    ): Flow<List<StoreResponse>> = flow {
        emit(
            httpClient.get {
                url("${CakkService.BASE_URL}${CakkService.Endpoint.STORE_LIST}")
                parameter(CakkService.Parameter.DISTRICT, district)
                parameter(CakkService.Parameter.PAGE, page)
            }
        )
    }

    fun fetchDetailStore(storeId: Int): Flow<StoreDetailResponse> = flow {
        emit(
            httpClient.get {
                url("${CakkService.BASE_URL}${CakkService.Endpoint.STORE_DETAIL}/$storeId")
            }
        )
    }
}
