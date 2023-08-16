package org.prography.localdb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.prography.localdb.converter.ImageListConverters
import org.prography.localdb.dao.BookmarkDao
import org.prography.localdb.entity.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
@TypeConverters(ImageListConverters::class)
abstract class CakkDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao
}
