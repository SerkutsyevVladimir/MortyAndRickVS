package com.example.rickandmortyvs.domain.mappers

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections

class Converters {

    @TypeConverter
    fun listToString(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToList(episodesString: String?): List<String> {
        if (episodesString == null) {
            return Collections.emptyList()
        }
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(episodesString, type)
    }
}