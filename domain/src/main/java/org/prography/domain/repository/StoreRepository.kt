package org.prography.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.prography.domain.model.store.FeedModel
import org.prography.domain.model.store.StoreBlogModel
import org.prography.domain.model.store.StoreDetailModel
import org.prography.domain.model.store.StoreModel

interface StoreRepository {
    fun fetchStoreList(district: String, storeTypes: String, page: Int): Flow<List<StoreModel>>

    fun fetchStoreType(storeId: Int): Flow<StoreModel>

    fun fetchDetailStore(storeId: Int): Flow<StoreDetailModel>

    fun fetchStoreBlog(storeId: Int): Flow<StoreBlogModel>

    fun fetchStoreReload(
        southwestLatitude: Double,
        southwestLongitude: Double,
        northeastLatitude: Double,
        northeastLongitude: Double,
        page: Int = 1,
        storeTypes: List<String> = listOf()
    ): Flow<List<StoreModel>>

    fun fetchStoreFeed(): Flow<PagingData<FeedModel>>

    fun fetchBookmarks(): Flow<List<StoreModel>>

    fun fetchBookmarkedCakeShop(id: Int): Flow<StoreModel?>

    suspend fun bookmarkStore(bookmarkModel: StoreModel)

    suspend fun unBookmarkStore(id: Int)
}
