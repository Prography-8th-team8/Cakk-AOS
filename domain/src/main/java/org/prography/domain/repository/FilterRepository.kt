package org.prography.domain.repository

import kotlinx.coroutines.flow.Flow

interface FilterRepository {
    fun fetchFilters(): Flow<String>

    suspend fun saveFilters(filters: String)
}
