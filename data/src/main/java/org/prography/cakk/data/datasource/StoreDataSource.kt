package org.prography.cakk.data.datasource

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.prography.network.CakkService.BASE_URL
import org.prography.network.CakkService.Endpoint.STORE_BLOG
import org.prography.network.CakkService.Endpoint.STORE_BLOG_NUM
import org.prography.network.CakkService.Endpoint.STORE_DETAIL
import org.prography.network.CakkService.Endpoint.STORE_LIST
import org.prography.network.CakkService.Endpoint.STORE_RELOAD
import org.prography.network.CakkService.Endpoint.STORE_TYPE
import org.prography.network.CakkService.Parameter.DISTRICT
import org.prography.network.CakkService.Parameter.NORTH_EAST_LATITUDE
import org.prography.network.CakkService.Parameter.NORTH_EAST_LONGITUDE
import org.prography.network.CakkService.Parameter.PAGE
import org.prography.network.CakkService.Parameter.SOUTH_WEST_LATITUDE
import org.prography.network.CakkService.Parameter.SOUTH_WEST_LONGITUDE
import org.prography.network.CakkService.Parameter.STORETYPE
import org.prography.network.CakkService.Parameter.STORE_TYPES
import org.prography.network.api.dto.response.*
import javax.inject.Inject

class StoreDataSource @Inject constructor(
    private val httpClient: HttpClient
) {
    fun fetchStoreList(district: String, storeTypes: String, page: Int): Flow<List<StoreResponse>> = flow {
        emit(
            httpClient.get {
                url("$BASE_URL$STORE_LIST")
                parameter(DISTRICT, district)
                parameter(STORETYPE, storeTypes)
                parameter(PAGE, page)
            }
        )
    }

    fun fetchStoreType(storeId: Int): Flow<StoreTypeResponse> = flow {
        emit(
            httpClient.get {
                url("$BASE_URL$STORE_DETAIL/$storeId/$STORE_TYPE")
            }
        )
    }

    fun fetchDetailStore(storeId: Int): Flow<StoreDetailResponse> = flow {
        emit(
            httpClient.get {
                url("$BASE_URL$STORE_DETAIL/$storeId")
            }
        )
    }

    fun fetchStoreBlog(storeId: Int, num: Int): Flow<StoreBlogResponse> = flow {
        emit(
            httpClient.get {
                url("$BASE_URL$STORE_DETAIL/$storeId/$STORE_BLOG")
                parameter(STORE_BLOG_NUM, num)
            }
        )
    }

    fun fetchStoreReload(
        southwestLatitude: Double,
        southwestLongitude: Double,
        northeastLatitude: Double,
        northeastLongitude: Double,
        page: Int,
        storeTypes: List<String>
    ): Flow<List<StoreReloadResponse>> = flow {
        emit(
            httpClient.get {
                url("$BASE_URL$STORE_RELOAD")
                parameter(SOUTH_WEST_LATITUDE, southwestLatitude)
                parameter(SOUTH_WEST_LONGITUDE, southwestLongitude)
                parameter(NORTH_EAST_LATITUDE, northeastLatitude)
                parameter(NORTH_EAST_LONGITUDE, northeastLongitude)
                parameter(PAGE, page)
                storeTypes.forEach { storeType ->
                    parameter(STORE_TYPES, storeType)
                }
            }
        )
    }
}
