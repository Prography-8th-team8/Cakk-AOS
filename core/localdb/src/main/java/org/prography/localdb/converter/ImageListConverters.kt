package org.prography.localdb.converter

import androidx.room.TypeConverter
import com.google.gson.Gson

class ImageListConverters {
    @TypeConverter
    fun listToJson(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {
        return Gson().fromJson(value, Array<String>::class.java)?.toList()
    }
}
