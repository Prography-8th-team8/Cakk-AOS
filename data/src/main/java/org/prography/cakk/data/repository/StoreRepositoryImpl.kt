package org.prography.cakk.data.repository

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.prography.cakk.data.BuildConfig
import org.prography.cakk.data.api.model.request.StoreListRequest
import org.prography.cakk.data.api.model.response.StoreListResponse
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : StoreRepository {

    override fun fetchStoreList(storeListRequest: StoreListRequest): Flow<List<StoreListResponse>> {
        return flow {
            httpClient.get {
                url("${BuildConfig.CAKK_BASE_URL}api/store/list")
                parameter("district", storeListRequest.district)
                parameter("page", storeListRequest.page)
            }
        }
    }
}