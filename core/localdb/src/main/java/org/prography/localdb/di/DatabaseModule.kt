package org.prography.localdb.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.prography.localdb.dao.BookmarkDao
import org.prography.localdb.database.CakkDatabase
import javax.inject.Singleton

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
}
