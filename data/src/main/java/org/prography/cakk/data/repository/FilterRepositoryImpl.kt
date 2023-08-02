package org.prography.cakk.data.repository

import kotlinx.coroutines.flow.Flow
import org.prography.cakk.data.datasource.FilterLocalDataSource
import org.prography.domain.repository.FilterRepository
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    private val filterLocalDataSource: FilterLocalDataSource
) : FilterRepository {
    override fun fetchFilters(): Flow<String> = filterLocalDataSource.fetchFilters()

    override suspend fun saveFilters(filters: String) = filterLocalDataSource.saveFilters(filters)
}
