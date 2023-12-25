package com.example.rickandmortyvs.data.database.models.episode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmortyvs.data.database.models.episode.DBEpisode.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME
)
data class DBEpisode(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = EPISODE_ID) val id: Int,
    @ColumnInfo(name = EPISODE_NAME) val name: String,
    @ColumnInfo(name = EPISODE_AIR_DATE) val airDate: String,
    @ColumnInfo(name = EPISODE_EPISODE) val episode: String,
    @ColumnInfo(name = EPISODE_CHARACTERS) val characters: List<String>,
    @ColumnInfo(name = EPISODE_URL) val url: String,
    @ColumnInfo(name = EPISODE_CREATED) val created: String

) {
    companion object {
        const val TABLE_NAME = "Episode"
        const val EPISODE_ID = "id"
        const val EPISODE_NAME = "name"
        const val EPISODE_AIR_DATE = "air_date"
        const val EPISODE_EPISODE = "episode"
        const val EPISODE_CHARACTERS = "characters"
        const val EPISODE_URL = "url"
        const val EPISODE_CREATED = "created"
    }
}