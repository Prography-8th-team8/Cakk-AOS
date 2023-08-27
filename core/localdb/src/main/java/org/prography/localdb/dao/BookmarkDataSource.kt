package org.prography.localdb.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.prography.localdb.entity.BookmarkEntity

@Dao
interface BookmarkDataSource {

    @Query("SELECT * FROM cake_store_bookmark_table")
    fun getAll(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM cake_store_bookmark_table WHERE id = :id")
    fun getCakeShop(id: Int): Flow<BookmarkEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkCakeStore(bookmarkEntity: BookmarkEntity)

    @Query("DELETE FROM cake_store_bookmark_table WHERE id = :id")
    suspend fun unBookmarkCakeStore(id: Int)
}
