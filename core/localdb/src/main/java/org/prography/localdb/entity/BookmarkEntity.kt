package org.prography.localdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import org.prography.domain.model.store.BookmarkModel

@Serializable
@Entity(tableName = "cake_store_bookmark_table")
data class BookmarkEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val district: String,
    val location: String,
    val imageUrls: List<String>,
    val bookmarked: Boolean,
) {
    fun toModel() = BookmarkModel(
        id = id,
        name = name,
        district = district,
        location = location,
        imageUrls = imageUrls,
        bookmarked = bookmarked,
    )
}
