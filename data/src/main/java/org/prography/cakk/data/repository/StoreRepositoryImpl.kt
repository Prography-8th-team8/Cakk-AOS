package org.prography.cakk.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.prography.cakk.data.datasource.FeedPagingSource
import org.prography.cakk.data.datasource.PAGE_SIZE
import org.prography.cakk.data.datasource.StoreRemoteSource
import org.prography.domain.model.store.BookmarkModel
import org.prography.domain.model.store.FeedModel
import org.prography.domain.model.store.StoreBlogModel
import org.prography.domain.model.store.StoreDetailModel
import org.prography.domain.model.store.StoreModel
import org.prography.domain.repository.StoreRepository
import org.prography.localdb.dao.BookmarkDao
import org.prography.localdb.entity.BookmarkEntity
import org.prography.utility.mapper.toModel
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao,
    private val storeRemoteSource: StoreRemoteSource,
    private val feedPagingSource: FeedPagingSource
) : StoreRepository {

    override fun fetchStoreList(district: String, storeTypes: String, page: Int): Flow<List<StoreModel>> =
        storeRemoteSource.fetchStoreList(district, storeTypes, page).map { it.toModel() }

    override fun fetchStoreType(storeId: Int): Flow<StoreModel> =
        storeRemoteSource.fetchStoreType(storeId).map { it.toModel() }

    override fun fetchDetailStore(storeId: Int): Flow<StoreDetailModel> =
        storeRemoteSource.fetchDetailStore(storeId).map { it.toModel() }

    override fun fetchStoreBlog(storeId: Int): Flow<StoreBlogModel> =
        storeRemoteSource.fetchStoreBlog(storeId).map { it.toModel() }

    override fun fetchStoreReload(
        southwestLatitude: Double,
        southwestLongitude: Double,
        northeastLatitude: Double,
        northeastLongitude: Double,
        page: Int,
        storeTypes: List<String>
    ): Flow<List<StoreModel>> =
        storeRemoteSource.fetchStoreReload(
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

    override fun fetchBookmarks(): Flow<List<BookmarkModel>> = bookmarkDao.getAll().map {
        it.map { data ->
            data.toModel()
        }
    }

    override suspend fun bookmarkStore(bookmarkModel: BookmarkModel) =
        bookmarkDao.bookmarkCakeStore(BookmarkEntity(bookmarkModel.id, bookmarkModel.name))

    override suspend fun unBookmarkStore(bookmarkModel: BookmarkModel) =
        bookmarkDao.unBookmarkCakeStore(BookmarkEntity(bookmarkModel.id, bookmarkModel.name))
}
