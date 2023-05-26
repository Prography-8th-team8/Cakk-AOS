package org.prography.cakk.data.repository

import org.prography.cakk.data.api.model.request.StoreListRequest
import org.prography.cakk.data.api.model.response.StoreListResponse

interface StoreRepository {

    suspend fun fetchStoreList(storeListRequest: StoreListRequest): List<StoreListResponse>
}
