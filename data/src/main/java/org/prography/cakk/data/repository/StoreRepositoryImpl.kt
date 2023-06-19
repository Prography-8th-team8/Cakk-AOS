package org.prography.cakk.data.repository

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.prography.cakk.data.api.dto.response.StoreDetailResponse
import org.prography.cakk.data.api.dto.response.StoreListResponse
import org.prography.domain.model.store.StoreDetailModel
import org.prography.domain.model.store.StoreModel
import org.prography.domain.repository.StoreRepository
import org.prography.network.CakkService.BASE_URL
import org.prography.network.CakkService.Endpoint.STORE_DETAIL
import org.prography.network.CakkService.Endpoint.STORE_LIST
import org.prography.network.CakkService.Parameter.DISTRICT
import org.prography.network.CakkService.Parameter.PAGE
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : StoreRepository {

    override fun fetchStoreList(
        district: String,
        page: Int
    ): Flow<List<StoreModel>> = flow {
        emit(
            httpClient.get<List<StoreListResponse>> {
                url("$BASE_URL$STORE_LIST")
                parameter(DISTRICT, district)
                parameter(PAGE, page)
            }.map { it.toModel() }
        )
    }

    override fun fetchDetailStore(storeId: Int): Flow<StoreDetailModel> = flow {
        emit(
            httpClient.get<StoreDetailResponse> {
                url("$BASE_URL$STORE_DETAIL/$storeId")
            }.toModel()
        )
    }
}
