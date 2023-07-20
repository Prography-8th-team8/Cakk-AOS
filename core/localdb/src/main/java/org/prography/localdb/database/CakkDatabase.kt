package org.prography.localdb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.prography.localdb.dao.BookmarkDao
import org.prography.localdb.entity.BookmarkData

@Database(entities = [BookmarkData::class], version = 1, exportSchema = false)
abstract class CakkDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao
}
