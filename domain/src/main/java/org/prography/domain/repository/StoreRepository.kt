package org.prography.domain.repository

import kotlinx.coroutines.flow.Flow
import org.prography.domain.model.store.StoreDetailModel
import org.prography.domain.model.store.StoreModel

interface StoreRepository {

    fun fetchStoreList(
        district: String,
        page: Int
    ): Flow<List<StoreModel>>

    fun fetchDetailStore(storeId: Int): Flow<StoreDetailModel>
}