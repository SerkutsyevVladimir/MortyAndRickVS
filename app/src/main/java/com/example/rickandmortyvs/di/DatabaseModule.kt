package com.example.rickandmortyvs.di

import android.content.Context
import androidx.room.Room
import com.example.rickandmortyvs.data.database.AppDatabase
import com.example.rickandmortyvs.data.database.dao.CharactersDao
import com.example.rickandmortyvs.data.database.dao.location.LocationsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DB_NAME
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCharactersDao(appDatabase: AppDatabase): CharactersDao {
        return appDatabase.getCharactersDao()
    }

    @Provides
    fun provideLocationsDao(appDatabase: AppDatabase): LocationsDao {
        return appDatabase.getLocationsDao()
    }

    companion object {
        private const val DB_NAME = "rick_and_morty_vs_app_database.db"
    }
}