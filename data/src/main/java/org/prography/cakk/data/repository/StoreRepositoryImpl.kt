package org.prography.cakk.data.repository

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.prography.cakk.data.api.model.request.StoreListRequest
import org.prography.cakk.data.api.model.response.StoreDetailResponse
import org.prography.cakk.data.api.model.response.StoreListResponse
import org.prography.network.CakkService.BASE_URL
import org.prography.network.CakkService.Endpoint.STORE_DETAIL
import org.prography.network.CakkService.Endpoint.STORE_LIST
import org.prography.network.CakkService.Parameter.DISTRICT
import org.prography.network.CakkService.Parameter.PAGE
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : StoreRepository {

    override fun fetchStoreList(storeListRequest: StoreListRequest): Flow<List<StoreListResponse>> {
        return flow {
            emit(
                httpClient.get {
                    url("$BASE_URL$STORE_LIST")
                    parameter(DISTRICT, storeListRequest.district)
                    parameter(PAGE, storeListRequest.page)
                }
            )
        }
    }

    override fun fetchDetailStore(storeId: Int): Flow<StoreDetailResponse> = flow {
        emit(
            httpClient.get {
                url("$BASE_URL$STORE_DETAIL/$storeId")
            }
        )
    }
}
