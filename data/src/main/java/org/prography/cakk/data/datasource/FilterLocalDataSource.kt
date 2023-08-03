package org.prography.cakk.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FilterLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun fetchFilters(): Flow<String> = dataStore.data.map { preferences ->
        preferences[FILTER] ?: ""
    }

    suspend fun saveFilters(filters: String) {
        dataStore.edit { preferences ->
            preferences[FILTER] = filters
        }
    }

    companion object {
        val FILTER = stringPreferencesKey("filter")
    }
}
