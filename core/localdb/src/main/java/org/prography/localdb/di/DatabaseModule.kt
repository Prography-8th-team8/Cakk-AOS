package org.prography.localdb.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.prography.localdb.dao.BookmarkDao
import org.prography.localdb.database.CakkDatabase
import javax.inject.Singleton

private const val FILTER_PREFERENCES = "filter_preferences"

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext context: Context): CakkDatabase = Room.databaseBuilder(
        context,
        CakkDatabase::class.java,
        "cakk.db"
    ).build()

    @Singleton
    @Provides
    fun provideBookmarkDao(cakkDatabase: CakkDatabase): BookmarkDao = cakkDatabase.bookmarkDao()

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() },
            ),
            migrations = listOf(SharedPreferencesMigration(context, FILTER_PREFERENCES)),
            scope = CoroutineScope(SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(FILTER_PREFERENCES) }
        )
    }
}
