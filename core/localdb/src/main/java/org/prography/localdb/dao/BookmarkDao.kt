package org.prography.localdb.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.prography.localdb.entity.BookmarkData

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM cake_store_bookmark_table")
    fun getAll(): Flow<List<BookmarkData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkCakeStore(bookmarkData: BookmarkData)

    @Delete
    suspend fun unBookmarkCakeStore(bookmarkData: BookmarkData)
}
