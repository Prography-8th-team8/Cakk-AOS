package org.prography.cakk.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import org.prography.network.CakkService.BASE_URL
import org.prography.network.CakkService.Endpoint.STORE_FEED
import org.prography.network.CakkService.Parameter.PAGE
import org.prography.network.api.dto.response.FeedResponse
import javax.inject.Inject

private const val STARTING_KEY = 1
const val PAGE_SIZE = 100

class FeedPagingSource @Inject constructor(
    private val httpClient: HttpClient
) : PagingSource<Int, FeedResponse>() {
    override fun getRefreshKey(state: PagingState<Int, FeedResponse>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedResponse> {
        try {
            val pageKey = params.key ?: STARTING_KEY
            val response = httpClient.get<List<FeedResponse>> {
                url("$BASE_URL$STORE_FEED?$PAGE=$pageKey")
            }
            return LoadResult.Page(
                response,
                prevKey = null,
                nextKey = if (response.isEmpty()) null else pageKey + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
