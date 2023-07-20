package org.prography.localdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.prography.domain.model.store.BookmarkModel

@Entity(tableName = "cake_store_bookmark_table")
data class BookmarkData(
    @PrimaryKey
    val id: Int,
    val name: String
) {
    fun toModel() = BookmarkModel(
        id = id,
        name = name
    )
}
