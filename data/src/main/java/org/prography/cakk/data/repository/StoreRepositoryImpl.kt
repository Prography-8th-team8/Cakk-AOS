package org.prography.cakk.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.prography.cakk.data.datasource.FeedPagingSource
import org.prography.cakk.data.datasource.PAGE_SIZE
import org.prography.cakk.data.datasource.StoreDataSource
import org.prography.domain.model.store.FeedModel
import org.prography.domain.model.store.StoreBlogModel
import org.prography.domain.model.store.StoreDetailModel
import org.prography.domain.model.store.StoreModel
import org.prography.domain.repository.StoreRepository
import org.prography.localdb.dao.BookmarkDataSource
import org.prography.localdb.entity.BookmarkEntity
import org.prography.utility.mapper.toModel
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val bookmarkDataSource: BookmarkDataSource,
    private val storeDataSource: StoreDataSource,
    private val feedPagingSource: FeedPagingSource
) : StoreRepository {

    override fun fetchStoreList(district: String, storeTypes: String, page: Int): Flow<List<StoreModel>> =
        storeDataSource.fetchStoreList(district, storeTypes, page).map { it.toModel() }

    override fun fetchStoreType(storeId: Int): Flow<StoreModel> =
        storeDataSource.fetchStoreType(storeId).map { it.toModel() }

    override fun fetchDetailStore(storeId: Int): Flow<StoreDetailModel> =
        storeDataSource.fetchDetailStore(storeId).map { it.toModel() }

    override fun fetchStoreBlog(storeId: Int, num: Int): Flow<StoreBlogModel> =
        storeDataSource.fetchStoreBlog(storeId, num).map { it.toModel() }

    override fun fetchStoreReload(
        southwestLatitude: Double,
        southwestLongitude: Double,
        northeastLatitude: Double,
        northeastLongitude: Double,
        page: Int,
        storeTypes: List<String>
    ): Flow<List<StoreModel>> =
        storeDataSource.fetchStoreReload(
            southwestLatitude = southwestLatitude,
            southwestLongitude = southwestLongitude,
            northeastLatitude = northeastLatitude,
            northeastLongitude = northeastLongitude,
            page = page,
            storeTypes = storeTypes
        ).map { it.toModel() }

    override fun fetchStoreFeed(): Flow<PagingData<FeedModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { feedPagingSource }
        ).flow
            .map { pagingData ->
                pagingData.map { response ->
                    response.toModel()
                }
            }
    }

    override fun fetchBookmarks(): Flow<List<StoreModel>> = bookmarkDataSource.getAll().map {
        it.map { data ->
            data.toModel()
        }
    }

    override fun fetchBookmarkedCakeShop(id: Int): Flow<StoreModel?> = bookmarkDataSource.getCakeShop(id).map {
        it?.toModel()
    }

    override suspend fun bookmarkStore(bookmarkModel: StoreModel) {
        bookmarkDataSource.bookmarkCakeStore(
            BookmarkEntity(
                bookmarkModel.id,
                bookmarkModel.name,
                bookmarkModel.district,
                bookmarkModel.location,
                bookmarkModel.imageUrls,
                bookmarkModel.bookmarked
            )
        )
    }

    override suspend fun unBookmarkStore(id: Int) =
        bookmarkDataSource.unBookmarkCakeStore(id)
}
