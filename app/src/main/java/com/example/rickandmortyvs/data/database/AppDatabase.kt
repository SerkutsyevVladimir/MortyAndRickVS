package com.example.rickandmortyvs.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmortyvs.data.database.dao.CharactersDao
import com.example.rickandmortyvs.data.database.dao.location.LocationsDao
import com.example.rickandmortyvs.data.database.models.DBCharacter
import com.example.rickandmortyvs.data.database.models.location.DBLocationDetails
import com.example.rickandmortyvs.domain.mappers.Converters

@Database(
    version = 1,
    entities = [
        DBCharacter::class,
        DBLocationDetails::class
    ],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getCharactersDao(): CharactersDao

    abstract fun getLocationsDao(): LocationsDao

}