package org.prography.localdb.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.prography.localdb.entity.BookmarkEntity

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM cake_store_bookmark_table")
    fun getAll(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkCakeStore(bookmarkEntity: BookmarkEntity)

    @Delete
    suspend fun unBookmarkCakeStore(bookmarkEntity: BookmarkEntity)
}
