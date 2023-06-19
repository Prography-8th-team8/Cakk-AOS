package org.prography.domain.repository.store

import kotlinx.coroutines.flow.Flow
import org.prography.cakk.data.api.model.request.StoreListRequest
import org.prography.cakk.data.api.model.response.StoreDetailResponse
import org.prography.cakk.data.api.model.response.StoreListResponse

interface StoreRepository {

    fun fetchStoreList(storeListRequest: StoreListRequest): Flow<List<StoreListResponse>>

    fun fetchDetailStore(storeId: Int): Flow<StoreDetailResponse>
}
