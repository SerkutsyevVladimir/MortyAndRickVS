package com.example.rickandmortyvs.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmortyvs.data.database.models.DBCharacter.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME
)
data class DBCharacter(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = CHARACTER_ID) val id: Int,
    @ColumnInfo(name = CHARACTER_NAME) val name: String,
    @ColumnInfo(name = CHARACTER_STATUS) val status: String,
    @ColumnInfo(name = CHARACTER_SPECIES) val species: String,
    @ColumnInfo(name = CHARACTER_TYPE) val type: String,
    @ColumnInfo(name = CHARACTER_GENDER) val gender: String,
    @Embedded(prefix = "origin_") val origin: DBOrigin,
    @Embedded(prefix = "location_") val location: DBLocation,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "episode") val episode: List<String>,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "created") val created: String,
) {
    companion object {
        const val TABLE_NAME = "Character"
        const val CHARACTER_ID = "id"
        const val CHARACTER_NAME = "name"
        const val CHARACTER_STATUS = "status"
        const val CHARACTER_SPECIES = "species"
        const val CHARACTER_TYPE = "type"
        const val CHARACTER_GENDER = "gender"
    }
}



