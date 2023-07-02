package org.prography.cakk.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.prography.cakk.data.datasource.StoreRemoteSource
import org.prography.domain.model.store.StoreBlogModel
import org.prography.domain.model.store.StoreDetailModel
import org.prography.domain.model.store.StoreModel
import org.prography.domain.repository.StoreRepository
import org.prography.utility.mapper.toModel
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeRemoteSource: StoreRemoteSource
) : StoreRepository {

    override fun fetchStoreList(district: String, page: Int): Flow<List<StoreModel>> =
        storeRemoteSource.fetchStoreList(district, page).map { it.toModel() }

    override fun fetchDetailStore(storeId: Int): Flow<StoreDetailModel> =
        storeRemoteSource.fetchDetailStore(storeId).map { it.toModel() }

    override fun fetchStoreBlog(storeId: Int): Flow<StoreBlogModel> =
        storeRemoteSource.fetchStoreBlog(storeId).map { it.toModel() }
}
