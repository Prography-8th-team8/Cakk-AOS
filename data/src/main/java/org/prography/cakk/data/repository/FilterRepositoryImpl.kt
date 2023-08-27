package org.prography.cakk.data.repository

import kotlinx.coroutines.flow.Flow
import org.prography.cakk.data.datasource.FilterDataSource
import org.prography.domain.repository.FilterRepository
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    private val filterDataSource: FilterDataSource
) : FilterRepository {
    override fun fetchFilters(): Flow<String> = filterDataSource.fetchFilters()

    override suspend fun saveFilters(filters: String) = filterDataSource.saveFilters(filters)
}
