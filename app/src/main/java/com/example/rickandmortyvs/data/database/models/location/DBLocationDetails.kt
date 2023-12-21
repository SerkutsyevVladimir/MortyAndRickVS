package com.example.rickandmortyvs.data.database.models.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmortyvs.data.database.models.location.DBLocationDetails.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME
)
data class DBLocationDetails(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = LOCATION_ID) val id: Int,
    @ColumnInfo(name = LOCATION_NAME) val name: String,
    @ColumnInfo(name = LOCATION_TYPE) val type: String,
    @ColumnInfo(name = LOCATION_DIMENSION) val dimension: String,
    @ColumnInfo(name = LOCATION_RESIDENTS) val residents: List<String>,
    @ColumnInfo(name = LOCATION_URL) val url: String,
    @ColumnInfo(name = LOCATION_CREATED) val created: String
) {
    companion object {
        const val TABLE_NAME = "Location"
        const val LOCATION_ID = "id"
        const val LOCATION_NAME = "name"
        const val LOCATION_TYPE = "type"
        const val LOCATION_DIMENSION = "dimension"
        const val LOCATION_RESIDENTS = "residents"
        const val LOCATION_URL = "url"
        const val LOCATION_CREATED = "created"
    }
}