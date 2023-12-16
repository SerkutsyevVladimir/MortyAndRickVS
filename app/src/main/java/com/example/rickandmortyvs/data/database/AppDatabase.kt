package com.example.rickandmortyvs.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmortyvs.data.database.dao.CharactersDao
import com.example.rickandmortyvs.data.database.models.DBCharacter

@Database(
    version = 1,
    entities = [
        DBCharacter::class
    ]
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getCharactersDao(): CharactersDao
}